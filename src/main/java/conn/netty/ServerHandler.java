package conn.netty;

import core.boot.config.Config;
import core.thread.Department;
import core.thread.Service;
import conn.ConnService;
import core.until.Log;
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
          conn = new ConnService(depart,connDepartId);
          depart.addService( conn );

      }catch (Exception e){
          Log.conn.error( " 链接错误 channle={} " , ctx ,this);
      }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
