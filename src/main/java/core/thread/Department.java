package core.thread;


import com.google.common.base.Stopwatch;
import core.req.Req;
import core.req.ReqResultBase;
import core.req.ReqTo;
import core.req.Task;
import core.thread.base.IThreadPlan;
import core.thread.base.ThreadImplementer;
import core.until.Log;
import core.until.Params;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Method;
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

    /**所属**/
    private  Headquarters head;

    public String getDepartmentId() {
        return departmentId;
    }

    /**当前业务描述*/
    private String departmentId;

    public String getId(){
        return departmentId;
    }


    /**最新一条消息的ID*/
    private long newReqID = 0;

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

    public void addReq(Req req){
        reqs.add(req);
    }

    public void addReqResult(Req req){
        reqResults.add(req);
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

        pulseService();


        try{//运行子类星耀
            pulseOveride();
        }catch (Throwable e){
            Log.core.error("运行子类心跳错误 depart={}",this,e);
        }

        //刷新本次星耀缓存
        flushReqbuffer();
    }

    /**
     * 调用下属服务
     */
    private void pulseService() {
        for (Service srv : services.values()) {
            try{
                srv.pulse();
            }catch (Throwable e){
                Log.core.error("调用下属服务出错 Service={}",srv,e);
            }
        }
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
        while(!pulseReqs.isEmpty()){
            Req req = pulseReqs.remove(0);
            receiveReq(req);
        }


    }

    /**
     * 处理一次请求求
     * @param req
     */
    private void receiveReq(Req req) {
        try{
            reqActiveStack.add( req );
            //执行请求
            Service serv = services.get( req.reqTo.serviceId );
            Method method = Service.getFunction(serv.getClass(), req.methodKey );
            method.setAccessible( true );
            method.invoke( serv,req.methodParam );
        }catch (Throwable e){
            Log.core.error( "Req请求错误，req={}",req,this );
        }finally {
            reqActiveStack.removeLast();
        }
    }

    /**
     * 处理本次返回值
     */
    private void pulseReqResults() {
     while (!pulseReqResults.isEmpty()){
         Req reqResult = pulseReqResults.remove( 0 );
         if (reqResultListener.containsKey( reqResult.id )){
             reqResultListener.get( reqResult.id ).listen( reqResult );
         }else{
             Log.core.error( "Req返回监听错误 req={}",reqResult,this );
         }

     }
    }

    /**
     * 处理超时监听
     */
//    private void pulseReqResultListenTimeOut() {
//        // todo
//    }

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

    /**
     * 启动当前业务线程
     * @param head
     */
    public void start(Headquarters head){
        this.head = head;
        this.head.putDepart(this);

        this.threadImpl.setName(toString());
        this.threadImpl.pluseon();
    }

    /**
     * 新Req申请id
     * @return
     */
    public long createReqID(){
        return ++newReqID;
    }

    public void addService(Service value){
        services.put( value.id,value );
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("HEAD",head)
                .append("departId",departmentId)
                .toString();
    }

    /**
     * 消息发送
     * @param to
     */
    public void req(ReqTo to,int methodKey,String srvId,Object...objects) {
        Req req = new Req();
        req.id = createReqID();
        req.fromDepartId = departmentId;
        req.fromrSvId = srvId;
        req.type = Req.Req_Type.RPC;
        req.reqTo = new ReqTo(to);
        req.methodKey = methodKey;
        req.methodParam = objects;

        head.handleReq(req);
    }

    public void returns(int methodKey,Object...objects){
        Req req = reqActiveStack.getLast().returnNew();
        req.id = createReqID();
        req.methodKey = methodKey;
        req.type = Req.Req_Type.RPC;
        req.methodParam = objects;

        head.handleReq(req);

    }

    public void returns(Params params){
        Req req = reqActiveStack.getLast().returnNew();
        req.returns = params;
        head.handleReq(req);
    }


}
