package login;

import core.note.function.DisMethod;
import core.note.clazz.DisServer;
import core.req.Escrow;
import core.thread.Department;
import core.thread.Service;
import core.until.Params;

import java.util.HashMap;
import java.util.HashSet;

@DisServer
//todo
public class UserGlobalService extends Service {
    private static final LoginMsgExtend msgHandle = new LoginMsgExtend();
    /**MethodKey*/
    /**消息分发**/
    public final static int LOGIN_METHOD_MSG_HANDLE = 0;

    /*********************************/
    MsgParam userGlobal = new MsgParam();


    static class GameLoacat{}
    public UserGlobalService(Department department,String id) {
        super( department,id );
    }

    @Override
    protected void pulseOverride() {

    }
//    @DisMethod( key =1 )
//    private void test(){}

    @DisMethod( key = LOGIN_METHOD_MSG_HANDLE )
    private void msgHandle(Object o){
        msgHandle.hadleMsg((Escrow) o,userGlobal);
    }


}
