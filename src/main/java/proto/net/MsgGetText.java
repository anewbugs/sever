package proto.net;


import proto.base.MsgBase;

public class MsgGetText extends MsgBase {
    public MsgGetText() {protoName = "MsgGetText";}
    //服务端回
    public String text = "";
}
