package game;

import core.note.clazz.DisServer;
import core.note.function.DisMethod;
import core.thread.Department;
import core.thread.Service;
@DisServer
//todo
public class GameService extends Service {
    /**MethodKey*/
    /**消息分发**/
    public final static int GAME_METHOD_MSG_HANDLE = 0;

    /*********************************/

    public GameService(Department department,String id) {
        super( department , id);
    }

    @Override
    protected void pulseOverride() {

    }

    @DisMethod(key=GAME_METHOD_MSG_HANDLE)
    private void msgHandle(){
        //todo
    }
}
