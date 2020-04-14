package room;

import core.note.clazz.DisServer;
import core.note.function.DisMethod;
import core.thread.Department;
import core.thread.Service;
import game.GameDepart;
import game.GameService;

import java.util.HashMap;

/**
 * @author wu
 * 全局房间服务
 */
@DisServer
//TODO
public class RoomGlobalService extends Service {
    /**MethodKey*/
    /**消息分发**/
    public final static int HALL_METHOD_MSG_HANDLE = 0;

    /*********************************/
    /**房间列表**/
    private HashMap<Long, GameDepart> rooms = new HashMap<>( );
    public RoomGlobalService(Department department ,String id) {
        super( department ,id);
    }

    @Override
    protected void pulseOverride() {

    }

    @DisMethod(key = HALL_METHOD_MSG_HANDLE)
    private void msgHandle(Object o){
        //todo
    }
}
