package com.github.rcf.client.service.api;

import com.github.rcf.factory.RcfServiceFactory;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public class ClientServiceApi {

    public ClientServiceApi() {

    }

    private static class SingletonHolder {
        static final ClientServiceApi instance = new ClientServiceApi();
    }

    public static ClientServiceApi getInstance() {
        return SingletonHolder.instance;
    }

    public Set<InetSocketAddress> getServersByGroup(String group) throws Exception{
        return RcfServiceFactory.getIClientService().getServersByGroup(group);
    }

    public void close() throws Exception{
        RcfServiceFactory.getIClientService().close();
    }

    public void connectZookeeper(String server, int timeout) throws Exception{
        RcfServiceFactory.getIClientService().connectZookeeper(server, timeout);
    }
}
