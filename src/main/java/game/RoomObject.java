package game;

import conn.ConnService;
import core.req.MsgContextBase;
import core.req.ReqTo;
import core.thread.Department;
import core.until.Log;
import data.enity.PlayerData;
import proto.base.Escrow;
import proto.base.PlayerInfo;
import proto.net.MsgGetRoomInfo;
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
    enum RoomStatus{
        PREPARE,
        FIGHT
    }
    /**阵营1-2,camp[1][1]:阵营1数量，camp[1][2]:阵营1死亡数量**/
    int[][] camp = new int[3][3];

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




    /**重新判定游戏结果**/
    private long lastjudgeTime = 0;
}
