package room;

import core.ob.MsgExtend;
import proto.base.Escrow;
import core.req.MsgContextBase;
import core.until.Log;

import java.util.Arrays;
import java.util.HashSet;

import static core.ob.Observer.ob;

public class RoomMsgExtend extends MsgExtend {
    private static final HashSet<String> roomMsgExtend = new HashSet<>(  );
    public static void reg(String...keys){
        roomMsgExtend.addAll( Arrays.asList( keys ) );
    }

    @Override
    public void fire(Escrow escrow, MsgContextBase params) {
        if (!roomMsgExtend.contains(escrow.msgName)){
            Log.msg.error("不是本阶段的消息 msgId={}",escrow.msgName);
            return;
        }
        ob.fire(escrow.msgName,escrow,params);
    }


}
