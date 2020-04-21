package login;

import core.boot.config.Config;
import core.req.MsgContextBase;

import java.util.HashMap;
import java.util.HashSet;

public class MsgLoginParam extends MsgContextBase {
    /**在线玩家**/
    private HashSet<String> logins ;
    /**掉线玩家**/
    private HashMap<String , GameLoacat> offLine;



    public MsgLoginParam() {

        this.logins = new HashSet<>(  );
        this.offLine = new HashMap<>();
    }

    public boolean loginAgain(String id){
        return offLine.containsKey( id );
    }

    public boolean isLost(String id){
        return offLine.containsKey( id );
    }

    public boolean applayLogin(String id){
        if(logins.size() > Config.LIMIT_LOGIN_SIZE) return false;
        return logins.add( id );
    }
}
