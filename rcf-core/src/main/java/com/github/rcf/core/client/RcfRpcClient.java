package com.github.rcf.core.client;

import com.github.rcf.core.client.factory.RcfRpcClientFactory;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public interface RcfRpcClient {


    public Object invokeImpl(String targetInstanceName, String methodName,
                             String[] argTypes, Object[] args, int timeout, int codecType, int protocolType)
            throws Exception;
    /**
     * server address
     *
     * @return String
     */
    public String getServerIP();

    /**
     * server port
     *
     * @return int
     */
    public int getServerPort();


    public RcfRpcClientFactory getClientFactory();
}
