package core.thread;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class Service {
    private static HashMap<Class<?>,Method[]> proxy = new HashMap();
    /**所属业务线程**/
    public Department department;
    /**服务ID**/
    public String id;


    public Service(Department department) {
        this.department = department;

    }

    public void pulse(){
        pulseThis();
        pulseOverride();

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
    private static Method getFunction(Class<?> clazz,int functionKey){
        Method[] functions = proxy.get(clazz);
        return functions[functionKey];
     }


}
