package data.dbservice;

import core.db.ResultMapAnnotation;
import data.enity.Account;
import data.mapper.AccountMapper;


public class AccountService {
    /**数据调用**/
    private AccountMapper accountMapper = ResultMapAnnotation.getMapper(AccountMapper.class);
    /**数据服务**/
    private PlayDataService dataMapper = new PlayDataService();

    public boolean checkLogin(String iduser ,String password){
        Account account = accountMapper.queryAccount( iduser,password );
        return account !=null;
    }

    public boolean register(String iduser,String password){
        if (accountMapper.queryOne( iduser ) != null)   return false;
        accountMapper.register(  iduser,password);
        dataMapper.createPlayerDate(  iduser);
        return true;

    }

}
