package core.boot;

import core.note.clazz.DisServer;
import core.note.function.DisMethod;
import core.note.function.MsgHandle;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;
import static core.ob.Observer.*;


/**
 * @author wu
 * 服务器启动类
 */
public class ServerApplication {
    /**
     * 程序启动总程序
     * @param primarySources
     * @param args
     */
    public static void run(Class<?> primarySources,String[] args){
        //反射扫描
        Reflections reflections = new Reflections();

        initServiceProxy( reflections.getTypesAnnotatedWith( DisServer.class ) );
        initMsgObserver( reflections.getMethodsAnnotatedWith( MsgHandle.class) );


    }

    /**
     * 初始化远程访问代理
     * @param clazzs
     */
    public static void  initServiceProxy(Set<Class<?>> clazzs){

    }

    /**
     * 初始化消息观察者
     * 用于消息分发
     * @param methods
     */
    public static void initMsgObserver(Set<Method> methods){
        for (Method method:methods) {
            ob.reg(method.getAnnotation( MsgHandle.class ).MsgID(),method);

        }
    }

}
