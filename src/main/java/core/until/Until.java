package core.until;



import core.cause.SException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;

/**
 * 法宝人、工具人
 */
public class Until {

    /**
     * 读取配置
     * @param path
     * @return
     */
    public static Properties readProperties(String path)  {

        InputStream in = Until.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties= new Properties();
        try{
            properties.load(in);
        }catch (IOException e){
            throw new SException(e);
        }
       return properties;
    }

    public static InputStream readFileFromResource(String path)  {

        InputStream in = Until.class.getClassLoader().getResourceAsStream("config.properties");

        return in;
    }

    /**
     * 字符串转Integer
     * @param value
     * @return
     */
    public static int transformInt(String value){
        if (StringUtils.isNotEmpty(value) && StringUtils.isNumeric(value)){
            return Integer.valueOf(value);
        }
        return 0;
    }

}
