package com.github.rcf.core.client.factory;

import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.client.AbstractRcfRpcClient;
import com.github.rcf.core.client.RcfRpcClient;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public interface RcfRpcClientFactory {

    public RcfRpcClient getClient(String host, int port) throws Exception;

    /**
     * 创建客户端
     * @param connectiontimeout  连接超时时间
     */
    public void startClient(int connectiontimeout);
    /**
     *
     * @param key
     * @param rpcClient
     */
    public void putRpcClient(String key,AbstractRcfRpcClient rpcClient);

    /**
     *
     * @param key
     */
    public void removeRpcClient(String key);


}
