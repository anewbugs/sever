package login;

import core.note.clazz.DisServer;
import core.thread.Department;
import core.thread.Service;

import java.util.HashMap;
import java.util.HashSet;

@DisServer
//todo
public class UserGlobalService extends Service {
    /**在线玩家**/


    private HashSet<String> logins = new HashSet<>(  );
    /**掉线玩家**/
    private HashMap<String ,GameLoacat> offLine = new HashMap<>();
    static class GameLoacat{}
    public UserGlobalService(Department department) {
        super( department );
    }

    @Override
    protected void pulseOverride() {

    }
}
