package room;

import core.req.ReqTo;
import org.apache.commons.lang3.StringUtils;

public class RoomLocation {
    private long roomId;
    private String departId;
    private int players = 0;

    public RoomLocation(long roomId, String departId) {
        this.roomId = roomId;
        this.departId = departId;
    }


    public int getPlayers() {
        return players;
    }

    public ReqTo getReqto(){
        return new ReqTo( departId,  String.valueOf( roomId ),"房间锁定" );
    }

    public void up(){
        players ++;
    }

    public void down(){
        players --;
    }

    public String getRoomId() {
        return roomId + "";
    }

}
