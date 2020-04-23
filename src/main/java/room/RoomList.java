package room;

import core.boot.config.Config;
import core.req.MsgContextBase;
import game.GameDepart;

import java.util.HashMap;

public class RoomList extends MsgContextBase {
    private long roomid = 0;
    /**房间列表**/
    private HashMap<String, RoomLocation> rooms = new HashMap<>( );

    /**
     * 申请房间id
     * @return
     */
    public long applayRoom(){
        return ++roomid;
    }

    /**
     * 添加房间
     * @param roomLocation
     * @return
     */
    public boolean addRoomLocation(RoomLocation roomLocation){
        if (rooms.size() + 1 < Config.LIMIT_ROOM_SIZE){
            rooms.put( roomLocation.getRoomId(),roomLocation );
            return true;
        }else {
            return false;
        }
    }


}
