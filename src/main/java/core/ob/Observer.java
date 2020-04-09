package core.ob;

import core.cause.SException;
import core.until.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author wu
 * 观察者模式
 * 主要用于消息的分发
 */
public class Observer {

    public static Observer ob = new Observer();

    /**观察者集**/
    private HashMap<String,Method> Observers = new HashMap<>();

    private Observer(){}

    public void reg(String key, Method valuse){
        if (!Observers.containsKey( key )){
            Observers.put( key, valuse );
        }else{
            throw new SException( "消息ID重复" );
        }
    }

    public void fire(String key ,Object...objects){
        try {
            Observers.get( key ).invoke( null,objects );
        } catch (Exception e) {
            Log.msg.error( "消息不存在 Msg={}", key, e );
        }
    }

}
