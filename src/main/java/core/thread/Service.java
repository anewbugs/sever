package core.thread;

import core.cause.SException;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class Service {
    private final static HashMap<Class<?>,Method[]> proxy = new HashMap();
    /**所属业务线程**/
    public Department department;
    /**服务ID**/
    public String id;


    /**
     * 构造方法
     * @param department
     */
    public Service(Department department) {
        this.department = department;

    }

    /**心跳**/
    public void pulse(){
        pulseThis();
        pulseOverride();

    }

    /**
     * 加载远程调用信息
     * @param key
     * @param value
     */
    public static  void reg(Class key ,Method[] value){
        if (!proxy.containsKey( key )){
            proxy.put( key , value );
        }else{
            throw new SException( key.getName() + "重复加载");
        }
    }


    /**当前心跳**/
    private  void pulseThis(){
        // todo 暂时没想到什么功能
    }

    /**子类心跳**/
    protected abstract void pulseOverride();


    /**
     * 获取具体调用的方法
     * @param clazz 调用具体类
     * @param functionKey 方法引用标志
     * @return
     */
    public static Method getFunction(Class<?> clazz,int functionKey){
        Method[] functions = proxy.get(clazz);
        return functions[functionKey];
     }


}
