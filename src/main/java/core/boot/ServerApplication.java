package core.boot;

import conn.ConnStart;
import core.boot.config.Config;
import core.cause.SException;
import core.note.clazz.DisServer;
import core.note.clazz.ImportPackage;
import core.note.function.DisMethod;
import core.note.function.MsgHandle;
import core.thread.Department;
import core.thread.Service;
import game.GameStart;
import login.LoginDepart;
import login.UserGlobalService;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import proto.base.ConfigMsgName;
import room.RoomDepart;
import room.RoomGlobalService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(primarySources.getAnnotation(ImportPackage.class).packages())
                        .addScanners(new MethodAnnotationsScanner() ) // 添加 方法注解扫描工具
                        .addScanners(new TypeAnnotationsScanner())
        );
        initServicesProxy( reflections.getTypesAnnotatedWith( DisServer.class ) );
        initMsgObserver( reflections.getMethodsAnnotatedWith( MsgHandle.class) );

        ConfigMsgName.msgLimitInit();

        //链接服务启动
        ConnStart.init( Config.SERVER_WORD_HEAD);
        //网络监听
        ConnStart.NetStart();

        //登陆服务启动
        Department login = new LoginDepart( Config.DEPART_Login_NAME);
        Service loginSrv = new UserGlobalService( login, Config.SRV_LOGIN_NAME );
        login.addService( loginSrv );
        login.start( Config.SERVER_WORD_HEAD );

        //房间列表服务
        Department room = new RoomDepart( Config.DEPART_ROOM_LIST_NAME );
        Service roomSrv = new RoomGlobalService( room,Config.SRV_ROOM_LIST_NAME );
        room.addService( roomSrv );
        room.start( Config.SERVER_WORD_HEAD  );

        //游戏服务线程组
        GameStart.init( Config.SERVER_WORD_HEAD );


    }

    /**
     * 初始化所有的业务服务远程调用
     * @param clazzs
     */
    public static void  initServicesProxy(Set<Class<?>> clazzs){
        for (Class clazz : clazzs){
            initServiceProxy(clazz);
        }
    }

    /**
     * 初始化单个业务远程服务
     * @param clazz
     */
    private static void initServiceProxy(Class clazz) {
        Method[] methods =clazz.getDeclaredMethods();
        //筛选
        List<Method> list = Arrays.stream( methods )
                .filter( e->e.isAnnotationPresent( DisMethod.class ))
                .collect( Collectors.toList());
        Method[] proxy = new Method[list.size()];
        try{
            for (Method method: list) {
                proxy[method.getAnnotation(DisMethod.class).key()] = method;
            }
        }catch (Throwable e){
            throw new SException( clazz.getName() + "远程索引有问题",e );
        }

        Service.reg( clazz,proxy );


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
