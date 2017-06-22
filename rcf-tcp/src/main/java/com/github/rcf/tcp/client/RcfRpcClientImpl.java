package com.github.rcf.tcp.client;

import com.github.rcf.core.callback.AsyncRPCCallback;
import com.github.rcf.tcp.client.factory.RcfTcpClientFactory;
import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.client.AbstractRcfRpcClient;
import com.github.rcf.core.client.factory.RcfRpcClientFactory;
import com.github.rcf.tcp.client.handler.RcfTcpClientHandler;
import io.netty.channel.ChannelFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfRpcClientImpl extends AbstractRcfRpcClient {

    private static final Log LOGGER = LogFactory.getLog(RcfRpcClientImpl.class);

    private RcfTcpClientHandler rcfTcpClientHandler;
    private Lock lock = new ReentrantLock();
    private Condition connectStatus = lock.newCondition();
    private Condition handlerStatus = lock.newCondition();

    private ChannelFuture future;

    public RcfRpcClientImpl(ChannelFuture future){
        this.future = future;
    }

    @Override
    public String getServerIP() {
        return ((InetSocketAddress) rcfTcpClientHandler.getChannel().remoteAddress()).getHostName();
    }

    @Override
    public int getServerPort() {
        return ((InetSocketAddress) rcfTcpClientHandler.getChannel().remoteAddress()).getPort();
    }

    @Override
    public RcfRpcClientFactory getClientFactory() {
        return RcfTcpClientFactory.getInstance();
    }

    @Override
    public AsyncRPCCallback sendRequest(final RcfRequest rcfRequest)
            throws Exception {
            AsyncRPCCallback callback = new AsyncRPCCallback(rcfRequest);
            RcfTcpClientHandler.mapCallBack.put(String.valueOf(rcfRequest.getId()),callback);
            if(future.channel().isOpen()){
                LOGGER.debug("客户端发送的requestId="+rcfRequest.getId());
                future.channel().writeAndFlush(rcfRequest);
            }else{
                LOGGER.error("客户端发送请求的通道异常"+rcfRequest.getId()+"丢失");
            }
            return callback;
  /*
           LOGGER.info("生成rcfrequest待发送="+rcfRequest.getId());
           return this.getRcfTcpClientHandler().sendRequest(rcfRequest);*/

    }

    @Override
    public void destroy() throws Exception {
        RcfTcpClientFactory.getInstance().removeRpcClient(rcfTcpClientHandler.getChannel().remoteAddress().toString());
        if(rcfTcpClientHandler.getChannel().isOpen()){
             rcfTcpClientHandler.getChannel().close();
        }
    }


    /**
     * 获取handler的时候要判断下是否为空，如果为空则设置其wait，否则为空会报空指针异常
     * @return
     * @throws Exception
     */
    public RcfTcpClientHandler getRcfTcpClientHandler()throws Exception {
        try{
            lock.lock();
            if (rcfTcpClientHandler == null) {
                connectStatus.await();
            }
            return rcfTcpClientHandler;

        }finally {
            lock.unlock();
        }

    }

    public void setRcfTcpClientHandler(RcfTcpClientHandler rcfTcpClientHandler) {
        try{
            lock.lock();
            this.rcfTcpClientHandler = rcfTcpClientHandler;
            handlerStatus.signal();
        }finally {
            lock.unlock();
        }
    }


}
