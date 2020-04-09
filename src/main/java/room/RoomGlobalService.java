package room;

import core.note.clazz.DisServer;
import core.thread.Department;
import core.thread.Service;
import game.GameDepart;
import game.GameService;

import java.util.HashMap;

/**
 * @author wu
 * 全局房间服务
 */
@DisServer
//TODO
public class RoomGlobalService extends Service {
    /**房间列表**/
    private HashMap<Long, GameDepart> rooms = new HashMap<>( );
    public RoomGlobalService(Department department ,String id) {
        super( department ,id);
    }

    @Override
    protected void pulseOverride() {

    }
}
