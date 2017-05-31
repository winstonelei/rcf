package com.github.rcf.core.server.process.impl;

import com.github.rcf.core.bean.RcfHttpBean;
import com.github.rcf.core.route.RpcServiceRouteMessage;
import com.github.rcf.core.server.process.AbstractRcfRpcHttpServerHandler;
import com.github.rcf.core.server.service.routeFactory.RcfRpcRouteServiceFactory;

import java.util.Map;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RpcHttpServerHandlerImpl extends AbstractRcfRpcHttpServerHandler {

    public static final int TYPE = 0;

    @Override
    public RpcServiceRouteMessage isRouteInfos(String route, String methodType, Map<String, Object> params) throws Exception {
        return RcfRpcRouteServiceFactory.getIRcfRpcRouteService().isRouteInfos(route,methodType,params);
    }

    @Override
    public Object methodInvoke(RpcServiceRouteMessage routeInfo) throws Exception {
        return RcfRpcRouteServiceFactory.getIRcfRpcRouteService().methodInvoke(routeInfo);
    }

    @Override
    public void registerProcessor(String instanceName, Object instance) {
        if(instance instanceof RcfHttpBean){
            RcfHttpBean rcfHttpBean = (RcfHttpBean) instance;
             RcfRpcRouteServiceFactory.getIRcfRpcRouteService().registerProcessor(instanceName,rcfHttpBean.getObject(),
                    rcfHttpBean.getHttpType(),rcfHttpBean.getReturnType());

        }
    }

    @Override
    public void clear() {
        RcfRpcRouteServiceFactory.getIRcfRpcRouteService().clear();
    }
}
