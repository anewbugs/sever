package core.thread;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池管理类
 * 单例模式
 * @author wu
 */
public class Headquarters {
    /**单例**/
    public static  Headquarters head = new Headquarters();
    /**管理的线程**/
    private ConcurrentHashMap<String,Department> departs = new ConcurrentHashMap();

    private Headquarters() {
    }

    /**
     * 查找要去的端点
     * @param key
     * @return
     */
    public Department getDeparts(String key) {
      return  departs.get( key );
    }

    /**
     * 服务器启动时才能添加
     * @param key
     * @param value
     */
    public void putDepart(String key ,Department value) {
        departs.put( key, value );
    }

}
