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

    protected static ConcurrentHashMap<Integer, LinkedBlockingQueue<Object>> responses =  new ConcurrentHashMap<Integer, LinkedBlockingQueue<Object>>();

    @Override
    public RcfRpcClient getClient(String host, int port) throws Exception {
        String key="/"+host+":"+port;
        if(rpcClients.containsKey(key)){
            return rpcClients.get(key);
        }
        return createClient(host, port);
    }

    protected abstract RcfRpcClient createClient(String targetIP, int targetPort) throws Exception;


    @Override
    public void putResponse(int key, LinkedBlockingQueue<Object> queue)
            throws Exception {
        responses.put(key, queue);
    }
    /**
     * 停止客户端
     */
    public abstract void stopClient() throws Exception;

    @Override
    public void receiveResponse(RcfResponse response) throws Exception {
        if (!responses.containsKey(response.getRequestId())) {
            LOGGER.error("give up the response,request id is:" + response.getRequestId() + ",maybe because timeout!");
            return;
        }
        try {
            if(responses.containsKey(response.getRequestId())){
                LinkedBlockingQueue<Object> queue = responses.get(response.getRequestId());
                if (queue != null) {
                    queue.put(response);
                } else {
                    LOGGER.warn("give up the response,request id is:"
                            + response.getRequestId() + ",because queue is null");
                }
            }

        } catch (InterruptedException e) {
             LOGGER.error("put response error,request id is:" + response.getRequestId(), e);
        }


    }

    @Override
    public void removeResponse(int key) {
        responses.remove(key);
    }

    public void clearClients(){
        rpcClients.clear();
    }

    @Override
    public void putRpcClient(String key, AbstractRcfRpcClient rpcClient) {
        rpcClients.put(key, rpcClient);
    }

    @Override
    public boolean containClient(String key){
        return rpcClients.containsKey(key);
    }

    @Override
    public void removeRpcClient(String key) {
        if(rpcClients.containsKey(key)){
            rpcClients.remove(key);
        }
    }
}
