package com.github.rcf.core.server.service.routeFactory;

import com.github.rcf.core.server.service.IRcfRpcRouteService;
import com.github.rcf.core.server.service.impl.RcfRpcRouteServiceImpl;

/**
 * Created by winstone on 2017/5/27.
 */
public class RcfRpcRouteServiceFactory {

    private static IRcfRpcRouteService rcfRpcRouteService = new RcfRpcRouteServiceImpl();

    public static IRcfRpcRouteService getIRcfRpcRouteService(){
        return rcfRpcRouteService;
    }
}
