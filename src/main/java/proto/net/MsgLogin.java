package proto.net;

import proto.base.MsgBase;

public class MsgLogin extends MsgBase {
    public MsgLogin() {protoName = "MsgLogin";}
    //客户端发
    public String id = "";
    public String pw = "";
    //服务端回（0-成功，1-失败）
    public int result = 0;

}


