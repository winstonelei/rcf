package com.github.rcf.tcp.client;

import com.github.rcf.tcp.client.factory.RcfTcpClientFactory;
import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.client.AbstractRcfRpcClient;
import com.github.rcf.core.client.factory.RcfRpcClientFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfRpcClientImpl extends AbstractRcfRpcClient {

    private static final Log LOGGER = LogFactory.getLog(RcfRpcClientImpl.class);

    private ChannelFuture future;

    public RcfRpcClientImpl(ChannelFuture future){
        this.future = future;
    }

    @Override
    public String getServerIP() {
        return ((InetSocketAddress) future.channel().remoteAddress()).getHostName();
    }

    @Override
    public int getServerPort() {
        return ((InetSocketAddress) future.channel().remoteAddress()).getPort();
    }

    @Override
    public RcfRpcClientFactory getClientFactory() {
        return RcfTcpClientFactory.getInstance();
    }

    @Override
    public void sendRequest(final RcfRequest rcfRequest)
            throws Exception {
        if(future.channel().isOpen()){
            ChannelFuture writeFuture = future.channel().writeAndFlush(rcfRequest);
            writeFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future)
                        throws Exception {
                    if (future.isSuccess()) {
                        return;
                    }
                    String errorMsg = "";
                    // write timeout

                    if (future.isCancelled()) {
                        errorMsg = "Send request to " + future.channel().toString()
                                + " cancelled by user,request id is:"
                                + rcfRequest.getId();
                    }else if (!future.isSuccess()) {
                        if (future.channel().isOpen()) {
                            future.channel().close();
                            getClientFactory().removeRpcClient(future.channel().remoteAddress().toString());
                        }
                        errorMsg = "Send request to " + future.channel().toString() + " error" + future.cause();
                    }
                    LOGGER.error(errorMsg);
                    RcfResponse response =
                            new RcfResponse(rcfRequest.getId(), rcfRequest.getCodecType());
                    response.setException(new Exception(errorMsg));
                    getClientFactory().receiveResponse(response);
                }
            });
        }

    }

    @Override
    public void destroy() throws Exception {
        RcfTcpClientFactory.getInstance().removeRpcClient(future.channel().remoteAddress().toString());
        if(future.channel().isOpen()){
            future.channel().close();
        }
    }


}
