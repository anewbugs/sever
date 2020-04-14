package login;

import core.note.function.MsgHandle;
import core.req.Escrow;
import core.req.MsgContextBase;
import proto.base.ConfigMsgName;

public class LoginManage {
    @MsgHandle(MsgID = ConfigMsgName.Login.MSG_LOGIN)
    public static void onMsgLogin(Escrow escrow, MsgParam msgParam){
        System.out.println("1");
    }
}
