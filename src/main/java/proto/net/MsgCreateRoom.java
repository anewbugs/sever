package proto.net;


import proto.base.MsgBase;

public class MsgCreateRoom extends MsgBase {
    public MsgCreateRoom() {protoName = "MsgCreateRoom";}
    //玩家id
    public String id ;
    //服务端回
    public int result = 0;
}
