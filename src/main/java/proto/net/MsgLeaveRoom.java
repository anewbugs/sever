package proto.net;


import proto.base.MsgBase;

public class MsgLeaveRoom extends MsgBase {
    public MsgLeaveRoom() {protoName = "MsgLeaveRoom";}
    //服务端回
    public int result = 0;
    //tankId
    public String id;
}
