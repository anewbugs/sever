package proto.net;


import proto.base.MsgBase;

public class MsgKick extends MsgBase {
    public MsgKick() {protoName = "MsgKick";}
    //原因（0-其他人登陆同一账号）
    public int reason = 0;
}
