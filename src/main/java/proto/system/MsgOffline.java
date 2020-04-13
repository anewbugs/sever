package proto.system;


import proto.base.MsgBase;

public class MsgOffline extends MsgBase {
    public MsgOffline() {
        protoName = "MsgOffline";
    }
    public String id;
    public int roomId;
}
