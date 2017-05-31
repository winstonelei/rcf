package com.github.rcf.tcp.client.proxy;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public interface ClientProxy {

    public <T> T getProxyService(Class<T> clazz, int timeout, int codecType,
                                 int protocolType, String targetInstanceName, String group);
}
