package com.github.rcf.tcp.client.invocation;

import com.github.rcf.tcp.client.factory.RcfTcpClientFactory;
import com.github.rcf.core.client.factory.RcfRpcClientFactory;
import com.github.rcf.core.client.invocation.AbstarctRcfInvocationHandler;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfTcpClientInvocationHandler extends AbstarctRcfInvocationHandler {

    public RcfTcpClientInvocationHandler(String group,
                                               int timeout,  String targetInstanceName,
                                               int codecType) {
        super(group, timeout, targetInstanceName, codecType);
    }

    public RcfRpcClientFactory getClientFactory() {
        return RcfTcpClientFactory.getInstance();
    }

}
