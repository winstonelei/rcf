package com.github.rcf.tcp.client.factory;

import com.github.rcf.tcp.client.RcfRpcClientImpl;
import com.github.rcf.tcp.client.handler.RcfTcpClientHandler;
import com.github.rcf.core.client.AbstractRcfRpcClient;
import com.github.rcf.core.client.RcfRpcClient;
import com.github.rcf.core.client.factory.AbstractRcfRpcClientFactory;
import com.github.rcf.core.thread.NamedThreadFactory;
import com.github.rcf.tcp.codec.RcfDecoderHandler;
import com.github.rcf.tcp.codec.RcfEncoderHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfTcpClientFactory  extends AbstractRcfRpcClientFactory {

    private static final Log LOGGER = LogFactory.getLog(RcfTcpClientFactory.class);

    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    private static final ThreadFactory workerThreadFactory = new NamedThreadFactory("NETTYCLIENT-WORKER-");

    private static EventLoopGroup workerGroup = new NioEventLoopGroup(6*PROCESSORS, workerThreadFactory);

    private final Bootstrap bootstrap = new Bootstrap();

    private static AbstractRcfRpcClientFactory clientFactory = new RcfTcpClientFactory();

   public static AbstractRcfRpcClientFactory getInstance(){
       return clientFactory;
   }


    @Override
    public void startClient(int connectTimeout) {
        LOGGER.info("----------------客户端服务开始启动-------------------------------");
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("decoder", new RcfDecoderHandler());
                pipeline.addLast("encoder", new RcfEncoderHandler());
                pipeline.addLast("timeout",new IdleStateHandler(0, 0, 120));
                pipeline.addLast("handler", new RcfTcpClientHandler());
            }

        });


        LOGGER.info("----------------客户端服务启动结束-------------------------------");
    }


    @Override
    protected RcfRpcClient createClient(String targetIP, int targetPort) throws Exception {
        String key="/"+targetIP+":"+targetPort;
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(targetIP, targetPort)).sync();
        future.awaitUninterruptibly();
        if (!future.isDone()) {
            LOGGER.error("Create connection to " + targetIP + ":" + targetPort + " timeout!");
            throw new Exception("Create connection to " + targetIP + ":" + targetPort + " timeout!");
        }
        if (future.isCancelled()) {
            LOGGER.error("Create connection to " + targetIP + ":" + targetPort + " cancelled by user!");
            throw new Exception("Create connection to " + targetIP + ":" + targetPort + " cancelled by user!");
        }
        if (!future.isSuccess()) {
            LOGGER.error("Create connection to " + targetIP + ":" + targetPort + " error", future.cause());
            throw new Exception("Create connection to " + targetIP + ":" + targetPort + " error", future.cause());
        }
        AbstractRcfRpcClient client = new RcfRpcClientImpl(future);
        super.putRpcClient(key, client);
        return client;
    }

    @Override
    public void stopClient() throws Exception {
        getInstance().clearClients();
        workerGroup.shutdownGracefully();
    }

}
