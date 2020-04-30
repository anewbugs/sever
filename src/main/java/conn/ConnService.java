package conn;

import core.boot.config.Config;
import core.note.clazz.DisServer;
import core.note.function.DisMethod;
import core.req.ReqTo;
import core.until.Params;
import data.enity.PlayerData;
import io.netty.buffer.ByteBuf;
import proto.base.ConfigMsgName;
import proto.base.Escrow;
import core.thread.Department;
import core.thread.Service;
import core.until.Log;
import core.until.TickTimer;
import game.GameService;
import io.netty.channel.Channel;
import login.UserGlobalService;
import proto.base.MsgBase;
import proto.net.MsgGetAchieve;
import room.RoomGlobalService;

import java.util.concurrent.ConcurrentLinkedQueue;

@DisServer
public class ConnService extends Service {
    /***proxy**/
    /**消息发回客户端**/
    public final static int CONN_METHOD_SEND_MSG = 0;
    /**更新链接客户端数据**/
    public final static int CONN_METHOD_UPDATE_STATUS_ID = 1;
    /**获取玩家数据**/
    public final static int CONN_METHOD_UPDATE_STATUS_ROOM = 2;
    /**更新玩家数据**/
    public final static int CONN_METHOD_UPDATE_USER = 3;
    /*********/



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
        //ping消息
        if (poll.msgName.equals( ConfigMsgName.SysMsg.MSG_PING )){
            sendMsg( poll );

        }else if (poll.msgName.equals( ConfigMsgName.User.MSG_GET_ACHIEVE)){
            onUeser( poll );
        }else {
            //非本地线程业务
            try {
                chanleCheck.reset();
                switch (connStatus.status) {
                    case Login:
                        department.req( connStatus.to, UserGlobalService.LOGIN_METHOD_MSG_HANDLE, id, new Object[]{poll} );
                        break;
                    case Hall:
                        poll.context = new Params("playerData", connStatus.humanObject.playerData.clone(),"departId",department.getId(),"srvId",id );
                        department.req( connStatus.to, RoomGlobalService.HALL_METHOD_MSG_HANDLE, id, new Object[]{poll/*,connStatus.humanObject.playerData.clone()*/} );
                        break;
                    case Room:
                        department.req( connStatus.to, GameService.GAME_METHOD_MSG_HANDLE, id, new Object[]{poll} );
                        break;
                }

            } catch (Throwable e) {
                Log.conn.error( "消息处理分发错误 msg={}", poll, e );
            }
        }

    }

    /**
     * 清理链接
     */
    private void clear(){}



    public void addMsgEscrow(Escrow escrow) {
      input.add( escrow );

    }

    /**
     * 消息发送
     * @param escrow
     */
    @DisMethod( key = CONN_METHOD_SEND_MSG )
    private void sendMsg(Escrow escrow){
        //todo
        ByteBuf byteBuf = this.channel.alloc().ioBuffer();
        byte[] protoNameSize = new byte[1];
        protoNameSize[0] = (byte)escrow.msgName.length();
        byteBuf.writeBytes( protoNameSize );
        byteBuf.writeBytes( escrow.msgName.getBytes() );
        byteBuf.writeBytes( escrow.msgByte );
        this.channel.writeAndFlush( byteBuf );
    }

    /**
     * 更新链接玩家数据
     * @param id
     */
    @DisMethod( key = CONN_METHOD_UPDATE_STATUS_ID)
    private void updateStatuse(String id){
        connStatus.updateHumanID( id );
    }

    /**
     * 更新当前状态为房间状态
     * @param to
     */
    @DisMethod( key = CONN_METHOD_UPDATE_STATUS_ROOM )
    private void updateRoom(ReqTo to){
        connStatus.setRoom( to.departmentId,to.serviceId );
        //department.returns( new Params( "playerData",connStatus.humanObject.playerData.clone() ,"departId",department.getId(),"srvId",this.id)) ;
    }

    @DisMethod( key = CONN_METHOD_UPDATE_USER )
    private void updateUser(PlayerData data){
        connStatus.humanObject.updateDate( data );
    }





    /**
     * 本地线程业务
     * @param escrow
     */
    public void onUeser(Escrow escrow){
        MsgGetAchieve msgGetAchieve = MsgBase.DecodeMsg( MsgGetAchieve.class,escrow.msgByte );
        msgGetAchieve.win = connStatus.humanObject.playerData.win;
        msgGetAchieve.lost = connStatus.humanObject.playerData.lost;
        sendMsg( Escrow.escrowBuilder( msgGetAchieve ) );
    }


}
