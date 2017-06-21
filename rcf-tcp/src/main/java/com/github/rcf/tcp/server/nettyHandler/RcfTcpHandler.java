package com.github.rcf.tcp.server.nettyHandler;

import com.github.rcf.core.bean.Constants;
import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.compiler.AccessAdaptiveProvider;
import com.github.rcf.core.server.process.handlerFactory.RcfRpcServerHandlerFactory;
import com.github.rcf.core.thread.CountableThreadPool;
import com.github.rcf.tcp.server.RcfTcpServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfTcpHandler extends ChannelInboundHandlerAdapter {

    private static final Log LOGGER = LogFactory.getLog(RcfTcpHandler.class);

    private int port;

    private int codecType;//编码类型

    public RcfTcpHandler(int threadCount, int port, int codecType) {
        super();
        this.port = port;
        this.codecType = codecType;
        RcfRpcServerHandlerFactory.getTcpServerHandler().registerProcessor(Constants.SYSTEM_PROPERITY_ACCESS_ADAPTIVE_PROVIDER,new AccessAdaptiveProvider());


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("远程主机ip和端口="+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e)
            throws Exception {
        if (!(e.getCause() instanceof IOException)) {
            // only log
            LOGGER.error("catch some exception not IOException", e);
        }
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (!(msg instanceof RcfRequest) ) {
            LOGGER.error("receive message error,only support RequestWrapper");
            throw new Exception(
                    "receive message error,only support RequestWrapper || List");
        }
        RcfRequest request = (RcfRequest) msg;
        RcfResponse response = new RcfResponse();
        LOGGER.info("服务端收到的消息id="+request.getId());
        ServerMessageHander task =  new ServerMessageHander((RcfRequest) msg,response);
        RcfTcpServer.submit(task,ctx,request,response);
    }


    private class ServerMessageHander implements Callable<Boolean>{
        private RcfRequest request = null;
        private RcfResponse response = null;

        ServerMessageHander(RcfRequest request,RcfResponse response){
            this.request = request;
            this.response = response;
        }

        public Boolean call() {
            try {
                response = RcfRpcServerHandlerFactory.getTcpServerHandler().handleRequestWithCallable(request, response);
                return Boolean.TRUE;
            }catch (Exception e){
                LOGGER.error(e.getMessage());
                return Boolean.FALSE;
            }
        }

    }
}
