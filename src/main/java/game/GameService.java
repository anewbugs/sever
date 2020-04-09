package game;

import core.note.clazz.DisServer;
import core.thread.Department;
import core.thread.Service;
@DisServer
//todo
public class GameService extends Service {

    public GameService(Department department,String id) {
        super( department , id);
    }

    @Override
    protected void pulseOverride() {

    }
}
