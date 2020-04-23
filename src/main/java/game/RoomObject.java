package game;

import core.req.MsgContextBase;
import core.req.ReqTo;
import core.until.Log;

import java.util.HashMap;
import java.util.HashSet;

public class RoomObject extends MsgContextBase {
    /**配置数据**/
    public static int maxPlayer = 6;
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
     * @param id
     * @param to
     * @return
     */
    public TankObject addTankObject(String id, ReqTo to) {
        TankObject tankObject = null;
        //房间人数
        if(tankList.size() >= maxPlayer){
            Log.game.warn("Room AddPlayer: room.AddPlayer fail, reach maxPlayer");
            return tankObject;
        }
        //准备状态才能加人
        if(status != RoomStatus.PREPARE){
            Log.game.warn("Room AddPlayer: room.AddPlayer fail, not PREPARE");
            return tankObject;
        }
        //已经在房间里
        if(tankList.containsKey(id)){
            Log.game.warn("Room AddPlayer: room.AddPlayer fail, already in this room");
            return tankObject;
        }
        tankObject = new TankObject( (camp[1][1] < camp[2][1] ? 1 : 2 ),id,to );
        tankList.put( id,tankObject );
        camp[tankObject.camp][1] ++;
        return tankObject;

//        //加入列表
//        playerIds.put(id,new RoomMember(SwitchCamp(),id));
//        //设置玩家数据
//        user.roomId = this.roomId;
//        //todo  player.setRoomId( this.id);
//        //设置房主
//        if(ownerId.equals("") ){
//            ownerId = user.getId();
//        }
//        //广播
//        Broadcast(ToMsg(new MsgGetRoomInfo()));
//        return true;
    }



    /**重新判定游戏结果**/
    private long lastjudgeTime = 0;
}
