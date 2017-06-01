package com.github.rcf.tcp.server;

import com.github.rcf.tcp.client.factory.RcfTcpClientFactory;
import com.github.rcf.tcp.codec.RcfDecoderHandler;
import com.github.rcf.tcp.codec.RcfEncoderHandler;
import com.github.rcf.core.server.RcfRpcServer;
import com.github.rcf.core.server.process.handlerFactory.RcfRpcServerHandlerFactory;
import com.github.rcf.core.thread.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import com.github.rcf.tcp.server.nettyHandler.RcfTcpHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfTcpServer  implements RcfRpcServer{

    private static final Log LOGGER = LogFactory.getLog(RcfTcpServer.class);

    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors()*2;

    private EventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;


    public int getCodecType() {
        return codecType;
    }

    public void setCodecType(int codecType) {
        this.codecType = codecType;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    private int codecType;//编码类型

    private int threadCount;//线程数

    public RcfTcpServer(){}

    private static class SingletonHolder {
        static final RcfTcpServer instance = new RcfTcpServer();
    }

    public static RcfTcpServer getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void registerProcessor(String serviceName, Object serviceInstance) {
        RcfRpcServerHandlerFactory.getTcpServerHandler().registerProcessor(serviceName,serviceInstance);
    }

    @Override
    public void stop() throws Exception {
        RcfRpcServerHandlerFactory.getTcpServerHandler().clear();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void start(final int port,final int timeout) throws Exception {
        ThreadFactory serverBossTF = new NamedThreadFactory("NETTYSERVER-BOSS-");
        ThreadFactory serverWorkerTF = new NamedThreadFactory("NETTYSERVER-WORKER-");
        bossGroup = new NioEventLoopGroup(PROCESSORS, serverBossTF);
        workerGroup = new NioEventLoopGroup(PROCESSORS * 2, serverWorkerTF);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535)
                .childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("decoder", new RcfDecoderHandler());
                pipeline.addLast("encoder", new RcfEncoderHandler());
                pipeline.addLast("timeout",new IdleStateHandler(0, 0, 120));
                pipeline.addLast("handler", new RcfTcpHandler(threadCount,port,codecType));

            }

        });
        LOGGER.info("-----------------开始启动--------------------------");
        bootstrap.bind(new InetSocketAddress(port)).sync();
        LOGGER.info("端口号："+port+"的 rcf tcp 服务端已经启动");
        LOGGER.info("-----------------启动结束--------------------------");
    }
}
