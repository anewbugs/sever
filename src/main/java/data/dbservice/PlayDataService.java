package data.dbservice;

import core.db.ResultMapAnnotation;
import data.enity.PlayerData;
import data.mapper.PlayDataMapper;

public class PlayDataService  {
    private PlayDataMapper  playdata = ResultMapAnnotation.getMapper(PlayDataMapper.class);


    public PlayerData queryplayerData(String iduser) {
        return playdata.queryplayerData( iduser );
    }


    public int updatePlayerData(PlayerData playerData) {
        return playdata.updatePlayerData( playerData );
    }


    public int updatePlayerData1(String iduser, int coin, String text, int win, int lost) {
        return playdata.updatePlayerData1( iduser, coin, text, win, lost );
    }


    public int createPlayerDate(String iduser) {
        return playdata.createPlayerDate( iduser );
    }
}
