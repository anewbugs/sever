package room;

import core.boot.config.Config;
import core.req.MsgContextBase;
import proto.base.RoomInfo;

import java.util.HashMap;

public class RoomList extends MsgContextBase {
    /**房间id**/
    private long roomid = 0;
    /**房间列表**/
    private HashMap<String, RoomLocation> rooms = new HashMap<>( );
    /**在申请中**/
    private long applayFor = 0;
    /**
     * 申请房间id
     * @return
     */
    public long applayRoom(){
        return ++roomid;
    }

    public int getRoomsSize(){
        return rooms.size();
    }

    /**
     * 添加房间
     * @param roomLocation
     * @return
     */
    public void addRoomLocation(RoomLocation roomLocation){
        rooms.put( roomLocation.getRoomId(),roomLocation );
    }

    public RoomLocation applayRoomLocation(){
        if (rooms.size() + applayFor + 1 > Config.LIMIT_ROOM_SIZE){
            return null;
        }
        long id = applayRoom();
        int gameDepartlast = (int)(id % Config.DEPART_GAME_SIZE);
        applayFor ++;
        return new RoomLocation(id , Config.DEPART_GAME_NAME + gameDepartlast);
    }

    public RoomLocation getRoom(String id) {
        return rooms.get( id );
    }

    public RoomInfo[] getList(){
        RoomInfo[] roomInfos = new RoomInfo[rooms.size()];
        int i = 0;
        for (RoomLocation roomLocation : rooms.values()){
            roomInfos[i] = roomLocation.getRoomInfo();
        }
        return roomInfos;
    }

    public void remove(String key){
        rooms.remove( key );
    }
}
