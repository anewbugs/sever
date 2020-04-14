package login;

import core.req.MsgContextBase;

import java.util.HashMap;
import java.util.HashSet;

public class MsgParam extends MsgContextBase {
    /**在线玩家**/
    private HashSet<String> logins ;
    /**掉线玩家**/
    private HashMap<String , UserGlobalService.GameLoacat> offLine;

    public MsgParam() {

        this.logins = new HashSet<>(  );
        this.offLine = new HashMap<>();
    }
}
