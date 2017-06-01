package com.github.rcf.core.client;

import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.serializable.RcfCodes;
import com.github.rcf.core.util.StringUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public abstract class AbstractRcfRpcClient implements  RcfRpcClient{

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



    private Object invokeImplIntern(RcfRequest rocketRPCRequest) throws Exception {
        long beginTime = System.currentTimeMillis();
        LinkedBlockingQueue<Object> responseQueue = new LinkedBlockingQueue<Object>(1);
        getClientFactory().putResponse(rocketRPCRequest.getId(), responseQueue);
        RcfResponse rcfResponse = null;
        try {

            sendRequest(rocketRPCRequest);

        }catch (Exception e) {
            rcfResponse = null;
            throw new RuntimeException("send request to os sendbuffer error", e);
        }
        Object result = null;
        try {

            result = responseQueue.poll(
                    rocketRPCRequest.getTimeout() - (System.currentTimeMillis() - beginTime),
                    TimeUnit.MILLISECONDS);
            System.out.println("pool时间:"+(System.currentTimeMillis() - beginTime));
        }finally{
            getClientFactory().removeResponse(rocketRPCRequest.getId());
        }
        if(result==null&&(System.currentTimeMillis() - beginTime)<=rocketRPCRequest.getTimeout()){//返回结果集为null
            rcfResponse =new RcfResponse(rocketRPCRequest.getId(), rocketRPCRequest.getCodecType());
        }else if(result==null&&(System.currentTimeMillis() - beginTime)>rocketRPCRequest.getTimeout()){//结果集超时
            String errorMsg = "receive response timeout("
                    + rocketRPCRequest.getTimeout() + " ms),server is: "
                    + getServerIP() + ":" + getServerPort()
                    + " request id is:" + rocketRPCRequest.getId();
           // LOGGER.error(errorMsg);
            rcfResponse=new RcfResponse(rocketRPCRequest.getId(), rocketRPCRequest.getCodecType());
            rcfResponse.setException( new Throwable(errorMsg));
        }else if(result!=null){
            rcfResponse = (RcfResponse) result;
        }

        try{
            if (rcfResponse.getResponse() instanceof byte[]) {
                String responseClassName = null;
                if(rcfResponse.getResponseClassName() != null){
                    responseClassName = new String(rcfResponse.getResponseClassName());
                }
                if(((byte[])rcfResponse.getResponse()).length == 0){
                    rcfResponse.setResponse(null);
                }else{
                    Object responseObject = RcfCodes.getDecoder(rcfResponse.getCodecType()).decode(
                            responseClassName,(byte[]) rcfResponse.getResponse());
                    if (responseObject instanceof Throwable) {
                        rcfResponse.setException((Throwable) responseObject);
                    }
                    else {
                        rcfResponse.setResponse(responseObject);
                    }
                }
            }
        }catch(Exception e){
          //  LOGGER.error("Deserialize response object error", e);
            throw new Exception("Deserialize response object error", e);
        }

        if (!StringUtils.isNullOrEmpty(rcfResponse.getException())) {
            Throwable t = rcfResponse.getException();
            //t.fillInStackTrace();
            String errorMsg = "server error,server is: " + getServerIP()
                    + ":" + getServerPort() + " request id is:"
                    + rocketRPCRequest.getId();
            return null;
        }

        return rcfResponse.getResponse();
    }

    /**
     * 发送请求
     * @throws Exception
     */
    public abstract void sendRequest(RcfRequest RcfRequest) throws Exception;

    public abstract void destroy() throws Exception;
}
