package com.github.rcf.core.server.process;

import com.github.rcf.core.route.RpcServiceRouteMessage;

import java.util.Map;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public abstract class AbstractRcfRpcHttpServerHandler implements RpcServerHandler {

    public abstract RpcServiceRouteMessage isRouteInfos(String route, String methodType, Map<String, Object> params)throws Exception;


    public abstract Object methodInvoke(RpcServiceRouteMessage routeInfo) throws Exception;
}
