package login;


import core.ob.MsgExtend;
import static core.ob.Observer.*;
import core.req.Escrow;
import core.until.Log;
import core.until.Params;

import java.util.HashSet;

public class LoginMsgExtend extends MsgExtend {
    private final static HashSet<String> loginMsgExtend = new HashSet<>();

    public static void reg(String key){
        loginMsgExtend.add(key);
    }

    @Override
    public void fire(Escrow escrow, Params params) {
        if (!loginMsgExtend.contains(escrow.msgName)){
            Log.msg.error("不是本阶段的消息 msgId={}",escrow.msgName);
            return;
        }
        ob.fire(escrow.msgName,escrow,params);
    }
}
