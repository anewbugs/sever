package game;

import core.req.Req;
import core.req.ReqTo;
import data.dbservice.PlayDataService;
import data.enity.PlayerData;
import proto.base.TankInfo;

public class TankObject {
    //*位置
    public float x;
    public float y;
    public float z;
    public float ex;
    public float ey;
    public float ez;

    //阵营
    public int camp = 1;
    //坦克生命值
    private int hp = 100;

    //tanid
    private String id;

    private ReqTo conn;

    public int getHp() {
        return hp;
    }

    public String getId() {
        return id;
    }

    public PlayerData getData() {
        return data;
    }

    private PlayerData data ;

    public TankObject(int camp, String id,PlayerData data, ReqTo to)  {
        this.camp = camp;
        this.id = id;
        conn = to;
        this.data = data;
    }



    //重新定位
    public void move(float x , float y , float z , float ex , float ey,float ez){
        this.x = x;
        this.y = y;
        this.z = z;
        this.ex = ex;
        this.ey = ey;
        this.ez = ez;
    }

    //玩家数据转成TankInfo
    public TankInfo PlayerToTankInfo(){
        TankInfo tankInfo = new TankInfo(
                this.getId(),
                this.camp,
                this.getHp(),
                this.x,
                this.y,
                this.z,
                this.ex,
                this.ey,
                this.ez);


        return tankInfo;
    }


    public ReqTo getConn() {
        return conn;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
