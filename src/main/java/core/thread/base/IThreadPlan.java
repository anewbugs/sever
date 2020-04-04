package core.thread.base;

/**
 * @author wu
 *
 * 线程运行接口
 */
public interface IThreadPlan {
    /**
     * 线程初始化
     */
    public void runInit();


    /**
     * 线程脉冲
     */
    public void runPulse();


    /**
     * 线程卸载
     */
    public void runUninstall();
}
