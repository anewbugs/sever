package login;

import core.note.function.MsgHandle;
import proto.base.Escrow;
import proto.base.ConfigMsgName;

public class LoginManage {
    @MsgHandle(MsgID = ConfigMsgName.Login.MSG_LOGIN)
    public static void onMsgLogin(Escrow escrow, MsgParam msgParam){
        System.out.println("1");
    }
}
