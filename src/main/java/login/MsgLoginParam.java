package login;

import core.boot.config.Config;
import core.req.MsgContextBase;
import core.req.ReqTo;
import core.until.Log;

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

    public GameLoacat getOffLocal(String humanID){
        return offLine.get( humanID );
    }

    public void reconned(String humanID){
        offLine.remove( humanID );
    }


    public boolean applayLogin(String id){
        if(logins.size() > Config.LIMIT_LOGIN_SIZE) return false;
        return logins.add( id );
    }

    public void lost(String key, ReqTo toRoom){
        offLine.put( key,new GameLoacat(toRoom) );
    }

    public void remove(String id) {
        if( offLine.remove( id ) != null){
            logins.remove( id );
            Log.login.debug( "玩家下线 id={}" ,id );
        }


    }
}
