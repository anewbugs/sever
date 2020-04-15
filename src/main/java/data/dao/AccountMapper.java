package data.dao;

import data.enity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface  AccountMapper {
    @Select("select * from user")
    public List<Account> queryAccont();
}
