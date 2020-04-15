package data.mapper;

import data.enity.PlayerData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PlayDataMapper {
    @Select("select * from playerdata where iduser=#{iduser}")
    PlayerData queryplayerData(@Param("iduser") String iduser);

    @Update("update playerdata set coin=#{coin},text =#{text},win =#{win},lost=#{lost} where iduser =#{iduser}")
    int updatePlayerData(PlayerData playerData);

    @Update("update game.playerdata set coin=#{coin},text =#{text},win =#{win},lost=#{lost} where iduser =#{iduser}")
    int updatePlayerData1(@Param("iduser") String iduser,@Param("coin") int coin,@Param("text") String text,@Param("win") int win, @Param("lost")int lost);

    @Insert("insert into playerdata(iduser) values (#{iduser})")
    int createPlayerDate(@Param("iduser") String iduser);



}
