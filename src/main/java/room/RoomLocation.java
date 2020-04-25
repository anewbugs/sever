package room;

import core.req.ReqTo;
import proto.base.RoomInfo;

public class RoomLocation {
    private long roomId;
    private String departId;
    private int players = 1;
    private int status = 0;

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

    public void setPlayers(int size){
        players = size;
    }
    public void addPlays(int i){
        players = players + i;
    }

    public String getRoomId() {
        return roomId + "";
    }

    public ReqTo getRommTo(){
        return new ReqTo(departId,getRoomId() ,"房间定位" ) ;
    }

    public int getIntId(){
        return (int) roomId;
    }

    public RoomInfo getRoomInfo(){
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.id = (int) roomId;
        roomInfo.count =players;
        roomInfo.status = status;
        return roomInfo;
    }

}
