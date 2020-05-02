package core.ob;

import proto.base.Escrow;
import core.req.MsgContextBase;
import core.until.Log;

public abstract class MsgExtend {
     /**
      *消息分发
      */
     public abstract void fire(Escrow escrow, MsgContextBase params);

     public void hadleMsg(Escrow msgEscrow ,MsgContextBase params){
          Log.core.debug("分发消息 msgId={}",msgEscrow.msgName);

          fire(msgEscrow,params);

     }


}
