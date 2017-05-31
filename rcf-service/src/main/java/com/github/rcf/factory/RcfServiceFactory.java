package com.github.rcf.factory;

import com.github.rcf.client.service.IClientService;
import com.github.rcf.client.service.impl.ClientServiceImpl;
import com.github.rcf.server.service.IserverService;
import com.github.rcf.server.service.impl.ServerServiceImpl;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public class RcfServiceFactory {

    private static IserverService serverService = new ServerServiceImpl();

    private static IClientService clientService = new ClientServiceImpl();

    public static IserverService getIserverService(){
        return serverService;
    }

    public static IClientService getIClientService(){
        return clientService;
    }
}
