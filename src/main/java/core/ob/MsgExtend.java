package core.ob;

import core.req.Escrow;
import core.req.MsgContextBase;
import core.until.Log;
import core.until.Params;

public abstract class MsgExtend {
     /**
      *消息分发
      */
     public abstract void fire(Escrow escrow, MsgContextBase params);

     public void hadleMsg(Escrow msgEscrow ,MsgContextBase params){
          Log.core.debug("接收到客户端消息 msgId={}",msgEscrow.msgName);

          fire(msgEscrow,params);

     }


}
