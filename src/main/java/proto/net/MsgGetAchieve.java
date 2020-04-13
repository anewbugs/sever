package proto.net;


import proto.base.MsgBase;

public class MsgGetAchieve extends MsgBase {
    public MsgGetAchieve() {protoName = "MsgGetAchieve";}
    //服务端回
    public String id ="";
    public int win = 0;
    public int lost = 0;
}
