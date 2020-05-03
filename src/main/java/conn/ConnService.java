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
import proto.net.MsgPong;
import room.RoomGlobalService;

import java.util.concurrent.ConcurrentLinkedQueue;

import static conn.ConnStatus.Status.*;

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

    private final static Escrow PONG = Escrow.escrowBuilder( new MsgPong() );

    /**netty通信通道**/
    private Channel channel;
    /**客户端消息队列**/
    private ConcurrentLinkedQueue<Escrow> input = new ConcurrentLinkedQueue();
    /**检查链接计时器**/
    private TickTimer connCheck = new TickTimer(10);
    /**玩家状态**/
    private ConnStatus connStatus = new ConnStatus();
    /**检查客户端断连**/
    private TickTimer chanleCheck = new TickTimer(Config.LOST_CHANNLE_TIME);

    private boolean clear = false;

    public ConnService(Department department, String id,Channel channel) {
        super( department, id );
        this.channel = channel;
    }


    @Override
    protected void pulseOverride() {
        pulseInput();
        channelCheck();
        clear();

    }

    /**
     * 确认连接
     */
    private void channelCheck(){
        //已经被检测断连
        if (this.clear){
            return;
        }

        if (!this.channel.isActive() || this.chanleCheck.isLost()){
            this.clear = true;
        }

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
        if (poll.msgName.equals( ConfigMsgName.SysMsg.MSG_PING ) ){
            sendMsg( PONG );

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
    private void clear(){
        //清理判断
        if (!clear){
            return;
        }

        if (connStatus.status == Hall){
            department.req( Config.TO_LOGIN, UserGlobalService.LOGIN_METHOD_HUMAN_LOST_3, id, new Object[]{null,connStatus.humanObject.humanID,false} );
        }else if (connStatus.status == Room){
            department.req( Config.TO_LOGIN, UserGlobalService.LOGIN_METHOD_HUMAN_LOST_3, id, new Object[]{connStatus.to,connStatus.humanObject.humanID,true} );
            department.req( connStatus.to,GameService.GAME_METHOD_TANK_LOST,id,new Object[]{connStatus.humanObject.humanID} );
        }

    }



    public void addMsgEscrow(Escrow escrow) {
      input.add( escrow );

    }

    /**
     * 消息发送
     * @param escrow
     */
    @DisMethod( key = CONN_METHOD_SEND_MSG )
    private void sendMsg(Escrow escrow){
        if (channel.isWritable()) {
            ByteBuf byteBuf = this.channel.alloc().ioBuffer();
            byte[] protoNameSize = new byte[ 1 ];
            protoNameSize[ 0 ] = (byte) escrow.msgName.length();
            byteBuf.writeBytes( protoNameSize );
            byteBuf.writeBytes( escrow.msgName.getBytes() );
            byteBuf.writeBytes( escrow.msgByte );
            this.channel.writeAndFlush( byteBuf );
        }else{
            //断连
            channelCheck();
        }
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
