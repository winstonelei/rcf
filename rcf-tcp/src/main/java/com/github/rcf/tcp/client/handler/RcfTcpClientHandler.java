package com.github.rcf.tcp.client.handler;

import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.callback.AsyncRPCCallback;
import com.github.rcf.tcp.client.factory.RcfTcpClientFactory;
import com.github.rcf.core.bean.RcfResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfTcpClientHandler  extends ChannelInboundHandlerAdapter {

    private static final Log LOGGER = LogFactory.getLog(RcfTcpClientHandler.class);

    private Map<String,AsyncRPCCallback> mapCallBack = new ConcurrentHashMap<String, AsyncRPCCallback>();

    private volatile Channel channel;

    private SocketAddress remoteAddr;

    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

    public RcfTcpClientHandler() {
        super();
    }



    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remoteAddr = this.channel.remoteAddress();
    }


    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }


    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        try{
            if (msg instanceof RcfResponse) {
                RcfResponse response = (RcfResponse) msg;
                if (isDebugEnabled) {
                    LOGGER.debug("receive response list from server: "
                            + ctx.channel().remoteAddress() + ",request is:"
                            + response.getRequestId());
                }
                AsyncRPCCallback callback = mapCallBack.get(String.valueOf(response.getRequestId()));
                if(callback!=null){
                    mapCallBack.remove(response.getRequestId());
                    callback.over(response);
                }

            } else {
                LOGGER.error("receive message error,only support List || ResponseWrapper");
                throw new Exception(
                        "receive message error,only support List || ResponseWrapper");
            }
        }finally{
            ReferenceCountUtil.release(msg);
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e)
            throws Exception {
        if (!(e.getCause() instanceof IOException)) {
            LOGGER.error("catch some exception not IOException", e);
        }
        RcfTcpClientFactory.getInstance().removeRpcClient(ctx.channel().remoteAddress().toString());
        if(ctx.channel().isOpen()){
            ctx.channel().close();
        }


    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.error("connection closed: " + ctx.channel().remoteAddress());
        RcfTcpClientFactory.getInstance().removeRpcClient(ctx.channel().remoteAddress().toString());
        if(ctx.channel().isOpen()){
            ctx.channel().close();
        }
    }


    public AsyncRPCCallback sendRequest(RcfRequest request){
        AsyncRPCCallback callback = new AsyncRPCCallback(request);
        mapCallBack.put(String.valueOf(request.getId()),callback);
        if(channel.isOpen()){
            channel.writeAndFlush(request);
        }
        return callback;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
