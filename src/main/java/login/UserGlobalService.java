package login;

import core.note.function.DisMethod;
import core.note.clazz.DisServer;
import core.thread.Department;
import core.thread.Service;

import java.util.HashMap;
import java.util.HashSet;

@DisServer
//todo
public class UserGlobalService extends Service {
    /**消息参数*/
    /**消息分发**/
    public final static int METHOD_MSGHANDLE = 0;

    /*********************************/



    /**在线玩家**/
    private HashSet<String> logins = new HashSet<>(  );
    /**掉线玩家**/
    private HashMap<String ,GameLoacat> offLine = new HashMap<>();
    static class GameLoacat{}
    public UserGlobalService(Department department,String id) {
        super( department,id );
    }

    @Override
    protected void pulseOverride() {

    }
//    @DisMethod( key =1 )
//    private void test(){}

    @DisMethod( key = METHOD_MSGHANDLE )
    private void msgHandle(Object o){

    }


}
