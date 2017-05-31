package com.github.rcf.core.server.service.routeFactory;

import com.github.rcf.core.server.service.IRcfRpcRouteService;
import com.github.rcf.core.server.service.impl.RcfRpcRouteServiceImpl;

/**
 * Created by winstone on 2017/5/27.
 */
public class RcfRpcRouteServiceFactory {

    private static IRcfRpcRouteService[] commonRpcRouteServices=new IRcfRpcRouteService[1];

    static{
        registerRouteService(RcfRpcRouteServiceImpl.TYPE, new RcfRpcRouteServiceImpl());
    }

    private static void registerRouteService(int type,IRcfRpcRouteService commonRpcRouteService){
        if(type > commonRpcRouteServices.length){
            IRcfRpcRouteService[] newServerHandlers = new IRcfRpcRouteService[type + 1];
            System.arraycopy(commonRpcRouteServices, 0, newServerHandlers, 0, commonRpcRouteServices.length);
            commonRpcRouteServices = newServerHandlers;
        }
        commonRpcRouteServices[type] = commonRpcRouteService;
    }

    public static IRcfRpcRouteService getIRcfRpcRouteService(){
        return commonRpcRouteServices[0];
    }
}
