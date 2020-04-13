package proto.net;


import proto.base.MsgBase;
import proto.base.PlayerInfo;

public class MsgGetRoomInfo extends MsgBase {
    public MsgGetRoomInfo() {protoName = "MsgGetRoomInfo";}
    //玩家id
    public String id ;

    //服务端回
    public PlayerInfo[] players;
}
