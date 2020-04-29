package game;

import core.req.Req;
import core.req.ReqTo;
import data.dbservice.PlayDataService;
import data.enity.PlayerData;
import proto.base.TankInfo;
import proto.net.MsgFire;
import proto.net.MsgHit;
import proto.net.MsgSyncTank;

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

    public boolean isDead(){
        return hp > 0;
    }

    //移动
    public boolean moving(MsgSyncTank msgSyncTank) {
        //移动验证
        if (isDead()){
            return false;
        }
        //数据验证
        move( msgSyncTank.x,msgSyncTank.y,msgSyncTank.z,msgSyncTank.ex,msgSyncTank.ey,msgSyncTank.ez );
        return true;
    }
    //
    public void firing(MsgFire msgFire) {
    }

    public boolean canFire() {
        if (isDead()){
            return false;
        }
        return true;
    }

    public void hurt(MsgHit msgHit) {
        if (msgHit.damage < 0){
            msgHit.damage = 35;
        }
            hp -= msgHit.damage;


    }
}
