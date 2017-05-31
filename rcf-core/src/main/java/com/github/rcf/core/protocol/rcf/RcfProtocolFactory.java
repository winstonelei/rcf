package com.github.rcf.core.protocol.rcf;

import com.github.rcf.core.protocol.RcfProtocol;
import com.github.rcf.core.protocol.impl.DefualtRpcProtocolImpl;

/**
 * Created by winstone on 2017/5/27.
 */
public class RcfProtocolFactory {

    private static RcfProtocol[] protocolHandlers = new RcfProtocol[2];

    static{
        registerProtocol(DefualtRpcProtocolImpl.TYPE, new DefualtRpcProtocolImpl());
    }

    private static void registerProtocol(int type,RcfProtocol customProtocol){
        if(type > protocolHandlers.length){
            RcfProtocol[] newProtocolHandlers = new RcfProtocol[type + 1];
            System.arraycopy(protocolHandlers, 0, newProtocolHandlers, 0, protocolHandlers.length);
            protocolHandlers = newProtocolHandlers;

        }
        protocolHandlers[type] = customProtocol;
    }

    public static RcfProtocol getProtocol(int type){
        return protocolHandlers[type];
    }
}
