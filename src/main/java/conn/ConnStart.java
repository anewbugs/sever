package conn;

import core.boot.config.Config;
import core.thread.Department;
import core.thread.Headquarters;

/**
 * 有关连接服务启动
 */
public class ConnStart {

    /**
     * 初始化连接服务
     * @param head
     */
    public static void init(Headquarters head){
        for (int i = 0; i < Config.DEPART_CONN_SIZE; i++) {
            Department depart = new ConnDepart(Config.DEPART_CONN_NAME + i);
           depart.start(head);
        }
    }

}
