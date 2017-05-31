package com.github.rcf.tcp.client.proxy.impl;

import com.github.rcf.tcp.client.invocation.RcfTcpClientInvocationHandler;
import com.github.rcf.tcp.client.proxy.ClientProxy;

import java.lang.reflect.Proxy;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfClientProxy implements ClientProxy {

    public RcfClientProxy(){}

    private static class SingletonHolder{
        static final RcfClientProxy instance = new RcfClientProxy();
    }

    public static RcfClientProxy getInstance(){
        return SingletonHolder.instance;
    }


    @Override
    public <T> T getProxyService(Class<T> clazz, int timeout, int codecType,
                                 int protocolType, String targetInstanceName, String group) {
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] { clazz },
                new RcfTcpClientInvocationHandler(group, timeout,
                        targetInstanceName, codecType, protocolType));
    }
}
