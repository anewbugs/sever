package core.db;

import java.io.InputStream;

import core.until.Until;
import data.dao.AccountMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class ResultMapAnnotation {
    private static SqlSession session;

//    private static GradeAnnotationInterface gradeInter=null;
//
//   private static StudentAnnotationInterfacestudentInter=null;

    static{
        //mybatis的配置文件
        String resource = "mybatis.xml";
        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = Until.readFileFromResource(resource);//ResultMapAnnotation.class.getResourceAsStream(resource);
        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
        // Readerreader = Resources.getResourceAsReader(resource);
        // 构建sqlSession的工厂
        // SqlSessionFactorysessionFactory = new SqlSessionFactoryBuilder().build(reader);
        // 创建能执行映射文件中sql的sqlSession
        session = sessionFactory.openSession();

//       gradeInter=session.getMapper(GradeAnnotationInterface.class);
//
//       studentInter=session.getMapper(StudentAnnotationInterface.class);
    }

    public static void main(String[] args) {
        AccountMapper accountMapper = session.getMapper(AccountMapper.class);
        System.out.println(accountMapper.queryAccont());
    }

}
