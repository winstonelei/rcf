package com.github.rcf.server.service.api;

import com.github.rcf.factory.RcfServiceFactory;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public class ServerServiceApi {

    public ServerServiceApi() {
    }
    private static class SingletonHolder {
        static final ServerServiceApi instance = new ServerServiceApi();
    }

    public static ServerServiceApi getInstance() {
        return SingletonHolder.instance;
    }


    public void registerService(String group, String server) throws Exception{
        RcfServiceFactory.getIserverService().registerServer(group, server);
    }

    public void close() throws Exception{
        RcfServiceFactory.getIserverService().close();
    }

    public void connectZookeeper(String server, int timeout) throws Exception{
        RcfServiceFactory.getIserverService().connectZookeeper(server, timeout);
    }

    public void registerClient(String server, String client) throws Exception{
        RcfServiceFactory.getIserverService().registerClient(server, client);
    }
}
