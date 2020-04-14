package game;

import core.ob.MsgExtend;
import core.req.Escrow;
import core.req.MsgContextBase;
import core.until.Log;
import core.until.Params;

import java.util.Arrays;
import java.util.HashSet;

import static core.ob.Observer.ob;

public class GameMsgExtend extends MsgExtend {
    private final static HashSet<String> gameMsgExtend = new HashSet();
    public static void reg(String...keys){
        gameMsgExtend.addAll( Arrays.asList( keys ) );
    }

    @Override
    public void fire(Escrow escrow, MsgContextBase params) {
        if (!gameMsgExtend.contains(escrow.msgName)){
            Log.msg.error("不是本阶段的消息 msgId={}",escrow.msgName);
            return;
        }
        ob.fire(escrow.msgName,escrow,params);
    }


}
