package proto.net;

import proto.base.MsgBase;
import proto.base.RoomInfo;

public class MsgGetRoomList extends MsgBase {
    public MsgGetRoomList() {protoName = "MsgGetRoomList";}
    public String id;
    //服务端回
    public RoomInfo[] rooms;
}
