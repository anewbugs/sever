package conn;

import core.req.Escrow;
import core.thread.Department;
import core.thread.Service;
import core.until.Log;
import core.until.TickTimer;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnService extends Service {
    /**掉线检查时间**/
    private static final int LOST_CHECK_TIME = 1000;
    /**netty通信通道**/
    private Channel channel;
    /**客户端消息队列**/
    private ConcurrentLinkedQueue<Escrow> input = new ConcurrentLinkedQueue();
    /**未收到消息的次数**/
    private int lostTimes = 0;
    /**检查链接计时器**/
    private TickTimer connCheck = new TickTimer(LOST_CHECK_TIME);
    /**玩家状态**/
    private ConnStatus status = new ConnStatus();

    public ConnService(Department department, String id,Channel channel) {
        super( department, id );
        this.channel = channel;
    }


    @Override
    protected void pulseOverride() {
        connCheck();
        pulseInput();
        clear();

    }

    /**
     * 确认连接
     */
    private void connCheck(){

    }

    /**
     * 消息分发
     */
    private void pulseInput(){
        if (input.isEmpty() ){
            if (connCheck.isLost()){
                lostTimes++;
                connCheck.reset();
            }
            return;
        }
        while(!input.isEmpty()){
            fireEscrow(input.poll());
        }

    }

    private void fireEscrow(Escrow poll) {
        try{

        }catch (Throwable e){
            Log.conn.error( "消息处理分发错误 msg={}" ,poll,e);
        }

    }

    /**
     * 清理链接
     */
    private void clear(){}



    public void addMsgEscrow(Escrow escrow) {
      input.add( escrow );

    }
}
