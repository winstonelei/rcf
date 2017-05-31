package com.github.rcf.tcp.client.handler;

import com.github.rcf.tcp.client.factory.RcfTcpClientFactory;
import com.github.rcf.core.bean.RcfResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfTcpClientHandler  extends ChannelInboundHandlerAdapter {

    private static final Log LOGGER = LogFactory.getLog(RcfTcpClientHandler.class);

    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();

    public RcfTcpClientHandler() {
        super();
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
                RcfTcpClientFactory.getInstance().receiveResponse(response);
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


}
