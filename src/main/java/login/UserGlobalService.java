package login;

import core.note.function.DisMethod;
import core.note.clazz.DisServer;
import core.req.ReqTo;
import proto.base.Escrow;
import core.thread.Department;
import core.thread.Service;

@DisServer
//todo
public class UserGlobalService extends Service {


    private static final LoginMsgExtend msgHandle = new LoginMsgExtend();
    /**MethodKey*/
    /**消息分发**/
    public final static int LOGIN_METHOD_MSG_HANDLE = 0;
    /**玩家掉线**/
    public static final int LOGIN_METHOD_HUMAN_LOST_3 = 1;
    /**清理数据**/
    public static final int LOGIN_METHOD_HUMAN_CLEAR = 2;
    /*********************************/
    /**
     * 服务器基础数据
     */
    MsgLoginParam userGlobal = new MsgLoginParam();



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

    @DisMethod( key = LOGIN_METHOD_HUMAN_LOST_3 )
    private void humanLost(ReqTo roomTo,String id ,boolean isRoom){
        if (isRoom){
            userGlobal.lost( id ,roomTo );
        }else{
            userGlobal.remove(id);
        }

    }

    @DisMethod( key = LOGIN_METHOD_HUMAN_CLEAR )
    private void humanClear(String humanID){
        userGlobal.remove( humanID);

    }

}
