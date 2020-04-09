package conn.netty;


import core.boot.config.Config;;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
public class Net extends Thread{
    //解码配置
    static final int MAX_FRAME_LENGTH = 1024;
    static final int LENGTH_FIELD_OFFSET = 0;
    static final int LENGTH_FIELD_LENGTH = 2;
    static final int LENGTH_ADJUSTMENT = 0;
    static final int INITIAL_BYTES_TO_STRIP = 0;

    @Override
    public void run() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        serverBootstrap
                .group(boosGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 10240)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {


                    protected void initChannel(NioSocketChannel nioSocketChannel) {
                        //消息长度处里Handler 解决半包和粘包问题
                        nioSocketChannel.pipeline().addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP));
                        //数据封包处理Handler
                        nioSocketChannel.pipeline().addLast("LengthFieldPrepender",new LengthFieldPrepender(LENGTH_FIELD_LENGTH));
                        //数据处理
                        nioSocketChannel.pipeline().addLast("ServerHandler",new ServerHandler());



                    }
                });
        bind(serverBootstrap, Config.LOCAL_PORT);
        System.out.println("网络服务启动*********************************");


    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {

        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.out.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
