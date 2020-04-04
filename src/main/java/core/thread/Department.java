package core.thread;


import com.google.common.base.Stopwatch;
import core.messagepack.Req;
import core.messagepack.ReqResultBase;
import core.messagepack.Task;
import core.thread.base.IThreadPlan;
import core.thread.base.ThreadImplementer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author wu
 * 业务部基类
 */
public class Department implements IThreadPlan {

    //todo  日志
    /**当前线程实例*/
    final static ThreadLocal<Department> currentDepart = new ThreadLocal<>();
    /**线程执行者*/
    private final ThreadImplementer threadImpl;


    /**当前业务描述*/
    private String departmentId;

    /**最新一条消息的ID*/
    private long newReqID;

    /**下属服务*/
    private Map<String, Service> services = new ConcurrentHashMap<>();

    /**请求处理栈*/
    private LinkedList<Req> reqActiveStack = new LinkedList<>();

    /**消息队列*/
    private ConcurrentLinkedQueue<Req> reqs = new ConcurrentLinkedQueue<Req>();

    /**消息返回值*/
    private ConcurrentLinkedQueue<Req> reqResults = new ConcurrentLinkedQueue<>();

    /**一次心跳处理的Req请求*/
    private List<Req> pulseReqs = new ArrayList<>();

    /**一次心跳接收到的返回值*/
    private List<Req> pulseReqResults = new ArrayList<>();

    /**port执行任务*/
    private ConcurrentLinkedQueue<Task> tasks = new ConcurrentLinkedQueue<>();

    /**执行计时器*/
    private Stopwatch pluseStepWatch = Stopwatch.createUnstarted();

    /**返回监听*/
    private Map<Long, ReqResultBase> reqResultListener = new HashMap<>();


    /**
     * 构造方法
     */
    public Department(String name){
        this.threadImpl = new ThreadImplementer(this);
        this.departmentId = name;
    }

    public static <T extends Department> T getCurrent() {
        return (T) currentDepart.get();
    }


    @Override
    public void runInit() {
        currentDepart.set(this);
    }

    @Override
    public void runPulse() {
        pulse();
    }


    @Override
    public void runUninstall() {
        currentDepart.set(null);
    }

    /**
     * 星耀
     */
    private void pulse() {
        //加载本次心跳的请求
        pulseReqLoad();

        //处理本次请求
        pulseReqs();

        //处理本次返回值
        pulseReqResults();

//        //清除超时监听
////        pulseReqResultListenTimeOut();

        //运行子类星耀
        pulseOveride();
        //刷新本次星耀缓存
        flushReqbuffer();
    }


    /**
     * 加载本次要处理的请求
     */
    private void pulseReqLoad() {
        //加载亲求
        while(!reqs.isEmpty()){
            pulseReqs.add(reqs.poll());
        }
        //加载返回值
        while(!reqResults.isEmpty()){
            pulseReqResults.add(reqResults.poll());
        }
    }

    /**
     * 处理本次请求
     */
    private void pulseReqs() {
        // todo
    }

    /**
     * 处理本次返回值
     */
    private void pulseReqResults() {
        // todo
    }

    /**
     * 处理超时监听
     */
    private void pulseReqResultListenTimeOut() {
        // todo
    }

    /**
     * 子类实现星耀
     */
    private void pulseOveride() {
    }

    /**
     * 清除本次心跳缓存
     */
    private void flushReqbuffer() {
        pulseReqs.clear();
        pulseReqResults.clear();

    }
}
