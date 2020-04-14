package conn;

import core.boot.config.Config;
import core.req.ReqTo;

public class ConnStatus {
    public enum Status {
        Login,
        Hall,
        Room

    }
    public ReqTo to ;
//    public String departId;
//    public String serviceId;

    public Status status;

    public ConnStatus() {
        this.status = Status.Login;
        this.to = new ReqTo(Config.DEPART_Login_NAME,Config.SRV_LOGIN_NAME,"login");

    }

    public void setHall(){
        this.status = Status.Hall;
        this.to.reSet(Config.DEPART_ROOM_LIST_NAME,Config.SRV_ROOM_LIST_NAME,"hall");

    }

    public void setRoom(String departId,String serviceId){
        this.status = Status.Room;
        this.to.reSet(departId,serviceId,"game");


    }
}
