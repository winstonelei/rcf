package com.github.rcf.core.client;

import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.callback.AsyncRPCCallback;
import com.github.rcf.core.serializable.RcfCodes;
import com.github.rcf.core.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public abstract class AbstractRcfRpcClient implements  RcfRpcClient{

    private static final Log LOGGER = LogFactory.getLog(AbstractRcfRpcClient.class);

    @Override
    public Object invokeImpl(String targetInstanceName, String methodName,
                             String[] argTypes, Object[] args, int timeout, int codecType) throws Exception {
        byte[][] argTypeBytes = new byte[argTypes.length][];
        for(int i =0; i < argTypes.length; i++) {
            argTypeBytes[i] =  argTypes[i].getBytes();
        }

        RcfRequest wrapper = new RcfRequest(targetInstanceName.getBytes(),
                methodName.getBytes(), argTypeBytes, args, timeout, codecType);

        return invokeImplIntern(wrapper);
    }



    private Object invokeImplIntern(RcfRequest rcfRPCRequest) throws Exception {
        try {
            AsyncRPCCallback callBack = sendRequest(rcfRPCRequest);
            return callBack.start();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("send request to os sendbuffer error", e);

        }
    }

    /**
     * 发送请求
     * @throws Exception
     */
    public abstract AsyncRPCCallback sendRequest(RcfRequest RcfRequest) throws Exception;

    public abstract void destroy() throws Exception;
}
