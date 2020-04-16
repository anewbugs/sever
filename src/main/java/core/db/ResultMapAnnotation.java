package core.db;

import java.io.IOException;
import java.io.Reader;

import data.enity.PlayerData;
import data.mapper.AccountMapper;
import data.enity.Account;
import data.mapper.PlayDataMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * mybatis配置
 * xml读取
 * mapper读取
 */

public class ResultMapAnnotation {
    private static SqlSession session;


    static{
        String resource = "mybatis.xml";
        try {
            Reader reader = Resources.getResourceAsReader(resource);
            // 构建sqlSession的工厂
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
            // 创建能执行映射文件中sql的sqlSession，并设自动提交事务
            session = sessionFactory.openSession(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //测试
    public static void main(String[] args) {
        PlayDataMapper playDataMapper = session.getMapper(PlayDataMapper.class);
        System.out.println( playDataMapper.updatePlayerData(new PlayerData("aa",110,"清零",110,0)));
        System.out.println(playDataMapper.queryplayerData("aa"));
    }

    public <T> T getMapper(Class<T> var1){
        return session.getMapper(var1);
    }

}
