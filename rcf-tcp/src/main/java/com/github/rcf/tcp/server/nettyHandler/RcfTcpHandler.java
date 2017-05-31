package com.github.rcf.tcp.server.nettyHandler;

import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.server.process.handlerFactory.RcfRpcServerHandlerFactory;
import com.github.rcf.core.thread.CountableThreadPool;
import com.github.rcf.factory.RcfServiceFactory;
import com.github.rcf.tcp.client.RcfRpcClientImpl;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfTcpHandler extends ChannelInboundHandlerAdapter {

    private static final Log LOGGER = LogFactory.getLog(RcfTcpHandler.class);

    private ThreadPoolExecutor threadPoolExecutor;

    private CountableThreadPool threadPool;

    private int port;

    private int procotolType;//协议名称

    private int codecType;//编码类型

    public RcfTcpHandler(int threadCount, int port,
                                     int procotolType, int codecType) {
        super();
        this.port = port;
        this.procotolType = procotolType;
        this.codecType = codecType;
        threadPoolExecutor= (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
        threadPool = new CountableThreadPool(threadCount);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.error("远程主机ip和端口="+ctx.channel().remoteAddress());
        RcfServiceFactory.getIserverService().registerClient(getLocalhost(), ctx.channel().remoteAddress().toString());
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
         //   LOGGER.error("catch some exception not IOException", e);
        }
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // TODO Auto-generated method stub

        if (!(msg instanceof RcfRequest) ) {
           // LOGGER.error("receive message error,only support RequestWrapper");
            throw new Exception(
                    "receive message error,only support RequestWrapper || List");
        }
        //threadPool.execute(new ServerHandlerRunnable(ctx,msg));
        threadPoolExecutor.submit(new ServerHandlerRunnable(ctx, msg));
    }
    /**
     * @param ctx
     * @param message
     */
    private void handleRequestWithSingleThread(final ChannelHandlerContext ctx,  Object message){
        RcfResponse rocketRPCResponse = null;
        try{
            RcfRequest request = (RcfRequest) message;
            rocketRPCResponse = RcfRpcServerHandlerFactory
                    .getTcpServerHandler().handleRequest(request, codecType,
                            procotolType);
            if(ctx.channel().isOpen()){
                LOGGER.error("11212remoteaddredss="+ctx.channel().remoteAddress());
                ChannelFuture wf = ctx.channel().writeAndFlush(rocketRPCResponse);
                wf.addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            LOGGER.error("11111server write response error,client  host is: " + ((InetSocketAddress) ctx.channel().remoteAddress()).getHostName()+":"+((InetSocketAddress) ctx.channel().remoteAddress()).getPort()+",server Ip:"+getLocalhost());
                            ctx.channel().close();
                        }
                    }
                });
            }

        }catch(Exception e){
            LOGGER.error(e.getMessage());

            sendErrorResponse(ctx, (RcfRequest) message,e.getMessage()+",server Ip:"+getLocalhost());
        }finally{
            ReferenceCountUtil.release(message);
        }
    }


    private void sendErrorResponse(final ChannelHandlerContext ctx, final RcfRequest request,String errorMessage) {
        RcfResponse commonRpcResponse =
                new RcfResponse(request.getId(), request.getCodecType(), request.getProtocolType());
        commonRpcResponse.setException(new Exception(errorMessage));
        ChannelFuture wf = ctx.channel().writeAndFlush(commonRpcResponse);
        wf.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    LOGGER.error("2222server write response error,request id is: " + request.getId()+",client Ip is:"+ctx.channel().remoteAddress().toString()+",server Ip:"+getLocalhost());
                    ctx.channel().close();
                }
            }
        });
    }

    private String getLocalhost(){
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            return ip+":"+port;
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("无法获取本地Ip",e);
        }

    }

    private class ServerHandlerRunnable implements Runnable{

        private  ChannelHandlerContext ctx;

        private  Object message;

        /**
         * @param ctx
         * @param message
         */
        public ServerHandlerRunnable(ChannelHandlerContext ctx, Object message) {
            super();
            this.ctx = ctx;
            this.message = message;
        }



        @Override
        public void run() {
            // TODO Auto-generated method stub
            handleRequestWithSingleThread(ctx, message);
        }

    }
}
