package com.github.rcf.core.protocol.rcf;

import com.github.rcf.core.protocol.RcfProtocol;
import com.github.rcf.core.protocol.impl.DefaultRpcProtocolImpl;

/**
 * Created by winstone on 2017/5/27.
 */
public class RcfProtocolFactory {

    private static RcfProtocol  rpcProtocol = new DefaultRpcProtocolImpl();

    public static RcfProtocol getProtocol(){
        return rpcProtocol;
    }
}
