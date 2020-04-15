package data.mapper;

import data.enity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface  AccountMapper {
    @Select("select * from user where iduser=#{iduser} and password=#{password} ")
     Account queryAccount(@Param("iduser") String iduser,@Param("password") String password);



    @Insert("insert into user(iduser,password) values(#{iduser},#{password})")
    int register(@Param("iduser") String iduser,@Param("password") String password);
}
