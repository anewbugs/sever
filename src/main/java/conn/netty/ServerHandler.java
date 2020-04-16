package conn.netty;

import core.boot.config.Config;
import proto.base.Escrow;
import core.thread.Department;
import conn.ConnService;
import core.until.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicLong;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**分配链接ID**/
    private final static AtomicLong newId = new AtomicLong( 0 );
    /**链接**/
    private ConnService conn;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
      try{
          long id = newId.addAndGet( 1 );
          int connNum = (int) (id % Config.DEPART_CONN_SIZE);
          String connDepartId = Config.DEPART_CONN_NAME + connNum;
          Department depart = Config.SERVER_WORD_HEAD.getDeparts( connDepartId );
          String srvId = Config.SRV_CONN_NAME + id;
          conn = new ConnService(depart,srvId,ctx.channel());
          depart.addService( conn );

      }catch (Exception e){
          Log.conn.error( " 链接错误 channle={} " , ctx ,this);
      }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf)msg;
        byte[] msgLengthBytes =new byte[2] ;
        requestByteBuf.readBytes(msgLengthBytes);
        int msgLength = (int)(((msgLengthBytes[0] << 8 ) & 0xff )|(msgLengthBytes[1] & 0xff));
        //解析协议名
        byte[] protoNameLengthBytes = new byte[1];
        requestByteBuf.readBytes(protoNameLengthBytes);
        int protoNameLength = (int)protoNameLengthBytes[0];
        byte[] protoNameBytes= new byte[protoNameLength];
        requestByteBuf.readBytes(protoNameBytes);
        String protoName = new String(protoNameBytes);
        //解析协议体
        byte[] jsonBytes = new byte[msgLength - protoNameLength - 1];
        requestByteBuf.readBytes(jsonBytes);
        requestByteBuf.release();
        this.conn.addMsgEscrow( new Escrow( protoName,jsonBytes ) );

    }
}
