package conn;

import core.boot.config.Config;

public class ConnStatus {
    public enum Status {
        Login,
        Hall,
        Room ;

    }
    public String departId;
    public String serviceId;

    private Status status;

    public ConnStatus() {
        this.status = Status.Login;
        this.departId = Config.DEPART_Login_NAME;
        this.serviceId = Config.SRV_LOGIN_NAME;
    }

    public void setHall(){
        this.status = Status.Hall;
        this.departId = Config.DEPART_ROOM_LIST_NAME;
        this.serviceId = Config.SRV_ROOM_LIST_NAME;
    }

    public void setRoom(String departId,String serviceId){
        this.status = Status.Room;
        this.departId = departId;
        this.serviceId = serviceId;

    }
}
