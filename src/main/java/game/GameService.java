package game;

import core.note.clazz.DisServer;
import core.note.function.DisMethod;
import core.req.ReqTo;
import core.thread.Department;
import core.thread.Service;
import core.until.Params;
import data.enity.PlayerData;
import proto.base.Escrow;
import proto.net.MsgGetRoomInfo;

@DisServer
//todo
public class GameService extends Service {


    private static GameMsgExtend msgExtend = new GameMsgExtend();
    /**MethodKey*/
    /**消息分发**/
    public final static int GAME_METHOD_MSG_HANDLE = 0;
    /**玩家加入**/
    public final static int GAME_METHOD_TADD_TANK = 1;
    /**玩家掉线**/
    public static final int GAME_METHOD_TANK_LOST = 2;
    /**重新连接**/
    public static final int GAME_METHOD_RECONNECT_2 = 3;
    /*********************************/
    private RoomObject roomObject ;

    public GameService(Department department,String id) {
        super( department , id);
        roomObject = new RoomObject( id );

    }

    public void initRoomObject(PlayerData data, ReqTo to){
        roomObject.addTankObject( data, to );
    }

    @Override
    protected void pulseOverride() {
        //房间心跳
        roomObject.puluse();
        //移除空房间
        if (roomObject.getPlayers() == 0){
            department.remove( roomObject.getRoomId() );
        }
    }

    @DisMethod(key=GAME_METHOD_MSG_HANDLE)
    private void msgHandle(Object o){
        msgExtend.hadleMsg((Escrow) o, roomObject );
    }

    @DisMethod( key = GAME_METHOD_TADD_TANK )
    private void addTank( PlayerData data, ReqTo to){
        boolean isAdd = roomObject.addTankObject(data,to);
        department.returns( new Params( isAdd  ) );
        // 广播
        if ( isAdd ){
            roomObject.multicast( Escrow.escrowBuilder( roomObject.getRoomInfo( new MsgGetRoomInfo() ) ) );

        }
    }

    @DisMethod( key = GAME_METHOD_TANK_LOST )
    private void tankLost(String humanID){
        roomObject.connLost(humanID);
    }

    @DisMethod( key = GAME_METHOD_RECONNECT_2 )
    private void tankReconnect(String humanID,ReqTo toConn){
        //todo 游戏重连

    }

}
