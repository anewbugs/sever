package core.ob;

import core.req.Escrow;
import core.until.Log;
import core.until.Params;

public abstract class MsgExtend {
     /**
      *消息分发
      */
     public abstract void fire(Escrow escrow, Params params);

     public void hadleMsg(Escrow msgEscrow ,Object...objects){
          Log.core.debug("接收到客户端消息 msgId={}",msgEscrow.msgName);

          fire(msgEscrow,new Params(objects));

     }


}
