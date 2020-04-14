package conn;

import core.boot.config.Config;
import core.req.Escrow;
import core.thread.Department;
import core.thread.Service;
import core.until.Log;
import core.until.TickTimer;
import game.GameService;
import io.netty.channel.Channel;
import login.UserGlobalService;
import room.RoomGlobalService;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnService extends Service {


    /**netty通信通道**/
    private Channel channel;
    /**客户端消息队列**/
    private ConcurrentLinkedQueue<Escrow> input = new ConcurrentLinkedQueue();
    /**未收到消息的次数**/
    private int lostTimes = 0;
    /**检查链接计时器**/
    private TickTimer connCheck = new TickTimer(10);
    /**玩家状态**/
    private ConnStatus connStatus = new ConnStatus();
    /**检查客户端断连**/
    private TickTimer chanleCheck = new TickTimer(Config.LOST_CHANNLE_TIME);


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

        while(!input.isEmpty()){
            fireEscrow(input.poll());
        }

    }

    /**
     * 分发消息
     * @param poll
     */
    private void fireEscrow(Escrow poll) {
        try{
            chanleCheck.reset();
            switch (connStatus.status){
                case Login:
                    department.req(connStatus.to, UserGlobalService.LOGIN_METHOD_MSG_HANDLE,this,poll);
                    break;
                case Hall:
                    department.req(connStatus.to, RoomGlobalService.HALL_METHOD_MSG_HANDLE,this,poll);
                    break;
                case Room:
                    department.req(connStatus.to, GameService.GAME_METHOD_MSG_HANDLE,this,poll);
                    break;
            }

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
