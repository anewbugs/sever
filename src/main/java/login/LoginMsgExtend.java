package login;


import core.ob.MsgExtend;
import static core.ob.Observer.*;
import proto.base.Escrow;
import core.req.MsgContextBase;
import core.until.Log;
import proto.base.ConfigMsgName;

import java.util.Arrays;
import java.util.HashSet;

public class LoginMsgExtend extends MsgExtend {


    private  static HashSet<String> loginMsgExtend = new HashSet<>();

    public static void reg(String...keys){
        loginMsgExtend.addAll( Arrays.asList( keys ) );
    }

    @Override
    public void fire(Escrow escrow, MsgContextBase params) {
        if (!loginMsgExtend.contains(escrow.msgName)){
            if (!escrow.msgName.equals(ConfigMsgName.SysMsg.MSG_PING)){
                Log.msg.error("不是本阶段的消息 msgId={}",escrow.msgName);
            }

            return;
        }
        ob.fire(escrow.msgName,escrow,params);
    }
}
