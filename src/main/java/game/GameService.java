package game;

import core.note.clazz.DisServer;
import core.note.function.DisMethod;
import core.req.ReqTo;
import core.thread.Department;
import core.thread.Service;
import core.until.Params;
import data.enity.PlayerData;
import proto.base.Escrow;

@DisServer
//todo
public class GameService extends Service {
    private static GameMsgExtend msgExtend = new GameMsgExtend();
    /**MethodKey*/
    /**消息分发**/
    public final static int GAME_METHOD_MSG_HANDLE = 0;
    /**玩家加入**/
    public final static int GAME_METHOD_TADD_TANK = 1;
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

    }

    @DisMethod(key=GAME_METHOD_MSG_HANDLE)
    private void msgHandle(Object o){
        msgExtend.hadleMsg((Escrow) o, roomObject );
    }

    @DisMethod( key = GAME_METHOD_TADD_TANK )
    private void addTank( PlayerData data, ReqTo to){
        department.returns( new Params( roomObject.addTankObject(data,to)  ) );
    }

}
