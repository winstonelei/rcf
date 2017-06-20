package com.github.rcf.core.client.factory;

import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.client.AbstractRcfRpcClient;
import com.github.rcf.core.client.RcfRpcClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public abstract  class AbstractRcfRpcClientFactory implements RcfRpcClientFactory {

    private static final Log LOGGER = LogFactory.getLog(AbstractRcfRpcClientFactory.class);

    protected static Map<String, AbstractRcfRpcClient> rpcClients = new ConcurrentHashMap<String, AbstractRcfRpcClient>();


    @Override
    public RcfRpcClient getClient(String host, int port) throws Exception {
        String key="/"+host+":"+port;
        if(rpcClients.containsKey(key)){
            return rpcClients.get(key);
        }
        return createClient(host, port);
    }

    protected abstract RcfRpcClient createClient(String targetIP, int targetPort) throws Exception;



    /**
     * 停止客户端
     */
    public abstract void stopClient() throws Exception;


    public void clearClients(){
        rpcClients.clear();
    }

    @Override
    public void putRpcClient(String key, AbstractRcfRpcClient rpcClient) {
        rpcClients.put(key, rpcClient);
    }


    @Override
    public void removeRpcClient(String key) {
        if(rpcClients.containsKey(key)){
            rpcClients.remove(key);
        }
    }



}
