package core.thread.base;


import com.google.common.base.Stopwatch;
import core.thread.base.IThreadPlan;

import java.util.concurrent.TimeUnit;

/**
 * 线程执行者
 * @author
 */
public class ThreadImplementer extends Thread{
    /**心跳间隔*/
    private static final int INTERVAL = 20;

    /**线程执行计划*/
    private IThreadPlan plan;

    /**线程运行标志*/
    private volatile boolean runFlag = false;

    /**线程中断标志*/
    private volatile boolean waitFlag = false;

    /**程序计时器*/
    private Stopwatch timer;

    public ThreadImplementer(IThreadPlan plan) {
        this.plan = plan;
        timer = Stopwatch.createUnstarted();
    }

    @Override
    public void run() {
        //执行准备
        plan.runInit();

        //执行脉冲
        while (runFlag){
           //捕获异常防止程序异常终止
            try {
                //等待判断
                if (waitFlag){
                    Thread.sleep(1);
                    continue;
                }

                //执行程序星跳
                timer.start();
                plan.runPulse();
                timer.stop();

                //星跳执行时间
                long runingTime = timer.elapsed( TimeUnit.MILLISECONDS );

                //避免极端情况下连续执行占用过多系统资源
                if (runingTime < INTERVAL){
                    Thread.sleep(INTERVAL - runingTime);
                }else {
                    Thread.sleep(1);
                }

            }catch (Throwable e){
                // todo 打印日志
                //error（e）；
            }finally {
                //重置程序执行计时器
                timer.reset();
            }
        }
    }

    /**
     * 启动
     */
    public void pluseon(){
        if (runFlag){
            return;
        }

        runFlag = true;

        start();
    }

    /**
     * 结束
     */
    public void closure(){
        if (!runFlag){
            return;
        }

        runFlag = false;
    }

    /**
     * 暂停
     */
    public void suspendT(){
        if (waitFlag){
            return;
        }

        waitFlag = true;
    }

    /**
     * 恢复
     */
    public void continueT(){
        if (!waitFlag){
            return;
        }

        waitFlag = false;
    }
}
