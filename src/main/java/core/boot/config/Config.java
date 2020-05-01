package core.boot.config;

import core.req.ReqTo;
import core.thread.Headquarters;
import core.until.Until;

import java.util.Properties;

public class Config {

    /**主世界**/
    public static final Headquarters SERVER_WORD_HEAD = new Headquarters();

    /**路径索引**/
    public static final String CONFIG_PATH = "config.properties";

    /**游戏线程数目**/
    public static final int DEPART_GAME_SIZE;
    /**游戏线程索引**/
    public static final String DEPART_GAME_NAME="game";

    /**连接线程数**/
    public static final int DEPART_CONN_SIZE;
    /**连接索引**/
    public static final String DEPART_CONN_NAME = "conn";
    /**登陆**/
    public static final String DEPART_Login_NAME = "login";

    /**大厅线程**/
    public static final String DEPART_ROOM_LIST_NAME = "hall";
    /**大厅服务**/
    public static final String SRV_ROOM_LIST_NAME = "roomList";



    /**游戏房间服务前缀**/
    //房间用纯数字
    public static final String SRV_GAME_NAME = "room";

    /**连接服务前缀**/
    public static final String SRV_CONN_NAME = "link";

    /**登陆服务前缀**/
    public static final String SRV_LOGIN_NAME = "online";

    /**登录数量限制**/
    public static final int LIMIT_LOGIN_SIZE;
    /**登录数量限制索引**/
    public static final String LIMIT_LOGIN_NAME = "login.size";

    /**房间数目限制**/
    public static final int LIMIT_ROOM_SIZE ;
    /**房间数目限制索引**/
    public static final String LIMIT_ROOM_NAME = "room.size";

    /**本地端口配置**/
    public static final int LOCAL_PORT;
    /**端口号**/
    public static final String LOCAL_PORT_name ="local.port";

    /**索引**/
    public static final String LOST_CHANNLE_NAME = "offline.time";
    /**Chanle断连**/
    public static final int  LOST_CHANNLE_TIME;
    /**大厅路径**/
    public static final ReqTo TO_HALL = new ReqTo( DEPART_ROOM_LIST_NAME,SRV_ROOM_LIST_NAME,"大厅路径");
    /**登陆路径**/
    public static final ReqTo TO_LOGIN = new ReqTo( DEPART_Login_NAME, SRV_LOGIN_NAME,"登陆路径");
    /**数据初始化**/
    static {
        Properties properties = Until.readProperties(CONFIG_PATH);
        //游戏
        DEPART_GAME_SIZE = Until.transformInt(properties.getProperty(DEPART_GAME_NAME) );
        //连接
        DEPART_CONN_SIZE = Until.transformInt(properties.getProperty(DEPART_CONN_NAME));
        //登录限制
        LIMIT_LOGIN_SIZE = Until.transformInt(properties.getProperty(LIMIT_LOGIN_NAME));
        //房间限制
        LIMIT_ROOM_SIZE  = Until.transformInt(properties.getProperty(LIMIT_ROOM_NAME));
        //端口号
        LOCAL_PORT = Until.transformInt(properties.getProperty(LOCAL_PORT_name));
        //掉线超时
        LOST_CHANNLE_TIME = Until.transformInt(properties.getProperty(LOST_CHANNLE_NAME));




    }

    public static void main(String[] args) {

    }
}
