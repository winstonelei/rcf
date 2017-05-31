package com.github.rcf.http.server;

import com.github.rcf.core.server.RcfRpcServer;
import com.github.rcf.core.server.process.handlerFactory.RcfRpcServerHandlerFactory;
import com.github.rcf.core.thread.NamedThreadFactory;
import com.github.rcf.http.server.handler.RcfHttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfHttpServer implements RcfRpcServer {

    private static final Log LOGGER = LogFactory.getLog(RcfHttpServer.class);

    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    private EventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    public RcfHttpServer(){}

    private static  class SingletonHandler{
        static final RcfHttpServer instance = new RcfHttpServer();
    }

    public static RcfHttpServer getInstance(){
        return  SingletonHandler.instance;
    }

    @Override
    public void registerProcessor(String serviceName, Object serviceInstance) {
        RcfRpcServerHandlerFactory.getHttpServerHandler().registerProcessor(serviceName,serviceInstance);//注册http服务
    }

    @Override
    public void stop() throws Exception {
        RcfRpcServerHandlerFactory.getHttpServerHandler().clear();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void start(int port, int timeout) throws Exception {
        ThreadFactory serverBossTF = new NamedThreadFactory("NETTYSERVER-BOSS-");
        ThreadFactory serverWorkerTF = new NamedThreadFactory("NETTYSERVER-WORKER-");
        bossGroup = new NioEventLoopGroup(PROCESSORS, serverBossTF);
        workerGroup = new NioEventLoopGroup(PROCESSORS * 2, serverWorkerTF);
        workerGroup.setIoRatio(50);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("codec", new HttpServerCodec());
                pipeline.addLast("aggegator", new HttpObjectAggregator(512 * 1024));
                pipeline.addLast("timeout",new IdleStateHandler(0, 0, 120));
                pipeline.addLast("biz", new RcfHttpServerHandler());
            }

        });
        LOGGER.info("-----------------开始启动--------------------------");
        bootstrap.bind(new InetSocketAddress(port)).sync();
        LOGGER.info("端口号："+port+"的 http 服务端已经启动");
        LOGGER.info("-----------------启动结束--------------------------");
    }
}
