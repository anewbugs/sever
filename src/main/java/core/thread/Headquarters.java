package core.thread;

import core.req.Req;
import core.until.Log;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池管理类
 * @author wu
 */
public class Headquarters {

    /**管理的线程**/
    private ConcurrentHashMap<String,Department> departs = new ConcurrentHashMap();

    public Headquarters() {
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
     * @param value
     */
    public void putDepart(Department value) {
        departs.put(value.getDepartmentId() , value );
    }

    /**
     * 消息转发
     * @param req
     */
    public void handleReq(Req req) {
        switch (req.type){
            case RPC:{
                Department department =departs.get(req.reqTo.departmentId);
                if (department != null){
                    department.addReq(req);
                }else{
                    Log.core.info("消息未找到接受请求的depart req={}",req);
                }
                break;
            }
            case RETURN:{
                Department department =departs.get(req.reqTo.departmentId);
                if (department != null){
                    department.addReqResult(req);
                }else{
                    Log.core.info("消息未找到接受请求的depart req={}",req);
                }
                break;
            }

        }
    }
}
