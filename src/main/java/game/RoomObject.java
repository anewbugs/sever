package game;

import conn.ConnService;
import core.req.MsgContextBase;
import core.req.ReqTo;
import core.thread.Department;
import core.until.Log;
import data.enity.PlayerData;
import proto.base.Escrow;
import proto.base.PlayerInfo;
import proto.base.TankInfo;
import proto.net.*;

import java.util.HashMap;
import java.util.HashSet;

public class RoomObject extends MsgContextBase {
    /**配置数据**/
    public static int maxPlayer = 6;

    /**
     *构造方法
     * @param roomId
     */
    public RoomObject(String roomId ) {
        this.roomId = roomId;

    }

    //出生点位置配置
    static float[][][] birthConfig =  {
            //阵营1出生点
            {
                    { 5f, 0f, -10f,     0f, 0f,0f},//出生点1
                    { 0f, 0f, -10f,     0f, 0f,0f},//出生点2
                    {-5f, 0f, -10f,     0f, 0f,0f},//出生点3
            },
            //阵营2出生点
            {
                    { 5f, 0f, 30f,     0f, 0f,0f},//出生点1
                    { 0f, 0f, 30f,     0f, 0f,0f},//出生点2
                    {-5f, 0f, 30f,     0f, 0f,0f},//出生点3
            },
    };




   /**房间id**/
    private String roomId;
    /**玩家列表**/
    private HashMap<String ,TankObject> tankList = new HashMap<>(  );
    /**掉线列表**/
    private HashSet<String > offline = new HashSet<>(  );
    /**房间拥有者**/
    private String roomOwner = "";
    /**房间状态**/
    private RoomStatus status = RoomStatus.PREPARE;
    /**阵营1-2,camp[1][1]:阵营1数量，camp[1][2]:阵营1死亡数量**/
    private int[][] camp = new int[3][3];
    /**重新判定游戏结果**/
    private long lastjudgeTime = 0;

    enum RoomStatus{
        PREPARE,
        FIGHT
    }
    //玩家移动
    public void moving(MsgSyncTank msgSyncTank) {
        if (status == RoomStatus.PREPARE){
            Log.game.error( "未开战 id={}" ,msgSyncTank.id);
            return;
        }
        TankObject tankObject = tankList.get( msgSyncTank.id );
        if (tankObject == null){
            Log.game.error( "该玩家不存在 id={}" ,msgSyncTank.id);
            return;
        }


        moving(msgSyncTank);
        multicast( Escrow.escrowBuilder( msgSyncTank ) );


    }
    //玩家开火
    public void firing(MsgFire msgFire) {
        if (status == RoomStatus.PREPARE){
            Log.game.error( "未开战 id={}" ,msgFire.id);
            return;
        }
        TankObject tankObject = tankList.get( msgFire.id );
        if (tankObject == null){
            Log.game.error( "该玩家不存在 id={}" ,msgFire.id);
            return;
        }

        if (!tankObject.canFire()){
            Log.game.error( "死亡不可开炮 id={}" ,msgFire.id);
            return;
        }


        multicast( Escrow.escrowBuilder( msgFire ) );
    }
    //集中协议
    public void hiting(MsgHit msgHit) {
        if (status == RoomStatus.PREPARE){
            Log.game.error( "未开战 id={}" ,msgHit.id);
            return;
        }
        TankObject tankObject = tankList.get( msgHit.id );
        if (tankObject == null){
            Log.game.error( "该玩家不存在 id={}" ,msgHit.id);
            return;
        }

        TankObject hitTankObject = tankList.get( msgHit.targetId );
        if (tankObject == null){
            Log.game.error( "被击中玩家不存在 id={}" ,msgHit.id);
            return;
        }

        if (!hurt(hitTankObject,msgHit)){
            Log.game.warn( "被击中玩家早以死亡 id={}" ,msgHit.targetId);
            return;
        }

        multicast( Escrow.escrowBuilder( msgHit ) );
    }

    private boolean hurt(TankObject hitTankObject, MsgHit msgHit) {
        if (hitTankObject.isDead()){
            return false;
        }
        hitTankObject.hurt(msgHit);
        if (hitTankObject.isDead()){
           camp[hitTankObject.camp][2]++;
        }

        return true;

    }




    /**
     * 玩家加入
     * @param to
     * @return
     */
    public boolean addTankObject(PlayerData data, ReqTo to) {
        TankObject tankObject = null;
        //房间人数
        if(tankList.size() >= maxPlayer){
            Log.game.warn("Room AddPlayer: room.AddPlayer fail, reach maxPlayer");
            return false;
        }
        //准备状态才能加人
        if(status != RoomStatus.PREPARE){
            Log.game.warn("Room AddPlayer: room.AddPlayer fail, not PREPARE");
            return false;
        }
        //已经在房间里
        if(tankList.containsKey(data.iduser)){
            Log.game.warn("Room AddPlayer: room.AddPlayer fail, already in this room");
            return false;
        }
        tankObject = new TankObject( (camp[1][1] < camp[2][1] ? 1 : 2 ),data.iduser,data,to );
        tankList.put( data.iduser,tankObject );
        camp[tankObject.camp][1] ++;
        /**创房者者为房间拥有者**/
        if (roomOwner.equals( "" )){
            roomOwner = data.iduser;
        }else{
            //组播
            multicast(
                    Escrow.escrowBuilder(
                            getRoomInfo(
                                    new MsgGetRoomInfo() ) ));
        }
        return true;
    }

    public boolean removeTankObject(String id){
        //准备状态才能删除
        if(status != RoomStatus.PREPARE){
            Log.game.warn("Room AddPlayer: room.AddPlayer fail, not PREPARE");
            return false;
        }

        TankObject tankObject = tankList.get( id );

        //不在房间里
        if( tankObject == null){
            Log.game.warn("Room AddPlayer: room.AddPlayer fail, already in this room");
            return false;
        }


        tankList.remove( id );
        camp[tankObject.camp][1] --;
        if (roomOwner.equals( id )){
            if (tankList.size() != 0){
                roomOwner = tankList.keySet().iterator().next();
            }

        }

        return true;
    }

    /**
     * 组播
     * @param escrow
     */
    public void multicast(Escrow escrow){
        for (TankObject tankObject :tankList.values()) {
            Department.getCurrent().req(
                    tankObject.getConn(),
                    ConnService.CONN_METHOD_SEND_MSG,
                    roomId,
                    new Object[]{escrow});
        }
    }

    public MsgGetRoomInfo getRoomInfo(MsgGetRoomInfo msgGetRoomInfo){
        int count = tankList.size();
        msgGetRoomInfo.players = new PlayerInfo[count];
        //players
        int i = 0;
        for(TankObject tankObject : tankList.values()){
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.id = tankObject.getId();
            playerInfo.camp = tankObject.camp;
            playerInfo.win = tankObject.getData().win;
            playerInfo.lost = tankObject.getData().lost;
            playerInfo.isOwner = 0;
            //房主判断
            if(roomOwner.equals( tankObject.getId() )){
                playerInfo.isOwner = 1;
            }
            msgGetRoomInfo.players[i] = playerInfo;
            i++;

        }
        return msgGetRoomInfo;
    }

    //能否开战
    public boolean CanStartBattle() {
        //已经是战斗状态
        if (status != RoomStatus.PREPARE){
            return false;
        }
        //对战双方人数
        if (camp[1][1] < 1 || camp[2][1]  < 1){
            return false;
        }
        return true;
    }

    //初始化位置
    private void SetBirthPos(TankObject tankObject, int index){
        int camp = tankObject.camp;
        tankObject.move(
                birthConfig[camp-1] [index][0],
                birthConfig[camp-1][ index][1],
                birthConfig[camp-1][ index][2],
                birthConfig[camp-1][ index][3],
                birthConfig[camp-1][ index][4],
                birthConfig[camp-1][ index][5]);


    }

    //重置玩家战斗属性
    private void ResetPlayers(){
        //位置和旋转
        int count1 = 0;
        int count2 = 0;
        for(TankObject tankObject : tankList.values()) {
            //RoomMember roomMember = playerIds.get(id);
            if(tankObject.camp == 1){
                SetBirthPos(tankObject, count1);
                count1++;
            }
            else {
                SetBirthPos(tankObject, count2);
                count2++;
            }

            tankObject.setHp( 100 );
        }
    }

    //开战
    public MsgEnterBattle StartBattle() {
        if(!CanStartBattle()){
            return null;
        }
        //状态
        status = RoomStatus.FIGHT;
        //玩家战斗属性
        ResetPlayers();
        //返回数据
        MsgEnterBattle msg = new MsgEnterBattle();
        msg.mapId = 1;
        msg.tanks = new TankInfo[tankList.size()];

        int i=0;
        for(TankObject tankObject : tankList.values()) {
            msg.tanks[i] = tankObject.PlayerToTankInfo();
            i++;
        }

        return msg;
    }

    /**
     * 模型心跳
     */
    public void puluse(){
        //todo
    }



}
