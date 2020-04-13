package proto.net;


import proto.base.MsgBase;
import proto.base.TankInfo;

public class MsgEnterBattle extends MsgBase {
    public MsgEnterBattle() {protoName = "MsgEnterBattle";}
    //服务端回
    public TankInfo[] tanks;
    public int mapId = 1;	//地图，只有一张
}
