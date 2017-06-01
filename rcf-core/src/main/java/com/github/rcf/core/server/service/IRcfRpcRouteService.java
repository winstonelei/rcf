package com.github.rcf.core.server.service;

import com.github.rcf.core.route.RpcServiceRouteMessage;

import java.util.Map;

/**
 * Created by winstone on 2017/5/27.
 */
public interface IRcfRpcRouteService {

    /**
     * 判断请求地址是否包含在路由表内，有则返回对应的实体
     * 如果有，设置参数;没有不设置，返回null
     * @param route
     * @param methodType
     * @param parmas
     * @return
     * @throws Exception
     */
    public RpcServiceRouteMessage isRouteInfos(String route,String methodType,Map<String, Object> parmas) throws Exception;

    /**
     * 根据参数执行对应的方法
     * @param routeInfo
     * @return
     * @throws Exception
     */
    public Object methodInvoke(RpcServiceRouteMessage routeInfo) throws Exception;

    /**
     * 注册服务
     * @param instance
     */
    public void registerProcessor(String projectname, Object instance,String httpType,String returnType);



    public void clear();
    
}
