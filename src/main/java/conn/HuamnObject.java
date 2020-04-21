package conn;

import data.dbservice.PlayDataService;
import data.enity.PlayerData;

public class HuamnObject {
    private static PlayDataService dataService = new PlayDataService();
    /**玩家id**/
    public String humanID;
    /**玩家数据**/
    public PlayerData playerData;

    public void update(String id){
        humanID = id;
        playerData = dataService.queryplayerData( id );
    }
}
