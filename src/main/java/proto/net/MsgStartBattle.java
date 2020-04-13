package proto.net;


import proto.base.MsgBase;

public class MsgStartBattle extends MsgBase {
    public MsgStartBattle() {protoName = "MsgStartBattle";}
    //tank id
    public String id ;
    //服务端回
    public int result = 0;
}
