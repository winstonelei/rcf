package com.github.rcf.core.client;

import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.serializable.RcfCodes;
import com.github.rcf.core.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public abstract class AbstractRcfRpcClient implements  RcfRpcClient{

    @Override
    public Object invokeImpl(String targetInstanceName, String methodName,
                             String[] argTypes, Object[] args, int timeout, int codecType,
                             int protocolType) throws Exception {
        byte[][] argTypeBytes = new byte[argTypes.length][];
        for(int i =0; i < argTypes.length; i++) {
            argTypeBytes[i] =  argTypes[i].getBytes();
        }

        RcfRequest wrapper = new RcfRequest(targetInstanceName.getBytes(),
                methodName.getBytes(), argTypeBytes, args, timeout, codecType, protocolType);

        return invokeImplIntern(wrapper);
    }


	/*public RemotingCommand invokeSyncImpl(final Channel channel, final RemotingCommand request, final long timeoutMillis)
			throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException {
		final int opaque = request.getOpaque();
		try {
			final ResponseFuture responseFuture = new ResponseFuture(opaque, timeoutMillis, null, null);
			this.responseTable.put(opaque, responseFuture);
			final SocketAddress addr = channel.remoteAddress();
			channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture f) throws Exception {
					if (f.isSuccess()) {
						responseFuture.setSendRequestOK(true);
						return;
					} else {
						responseFuture.setSendRequestOK(false);
					}

					responseTable.remove(opaque);
					responseFuture.setCause(f.cause());
					responseFuture.putResponse(null);
					plog.warn("send a request command to channel <" + addr + "> failed.");
				}
			});

			RemotingCommand responseCommand = responseFuture.waitResponse(timeoutMillis);
			if (null == responseCommand) {
				if (responseFuture.isSendRequestOK()) {
					throw new RemotingTimeoutException(RemotingHelper.parseSocketAddressAddr(addr), timeoutMillis,
							responseFuture.getCause());
				} else {
					throw new RemotingSendRequestException(RemotingHelper.parseSocketAddressAddr(addr), responseFuture.getCause());
				}
			}

			return responseCommand;
		} finally {
			this.responseTable.remove(opaque);
		}
	}*/

    private Object invokeImplIntern(RcfRequest rocketRPCRequest) throws Exception {
        long beginTime = System.currentTimeMillis();
        LinkedBlockingQueue<Object> responseQueue = new LinkedBlockingQueue<Object>(1);
        getClientFactory().putResponse(rocketRPCRequest.getId(), responseQueue);
        RcfResponse rcfResponse = null;
        final int opaque = rocketRPCRequest.getId();

        try {

//			final ResponseFuture responseFuture = new ResponseFuture(opaque, rocketRPCRequest.getTimeout(), null, null);
//			this.responseTable.put(opaque, responseFuture);
            //final SocketAddress addr = channel.remoteAddress();

          /*  if(LOGGER.isDebugEnabled()){
                LOGGER.debug("client ready to send message,request id: "+rocketRPCRequest.getId());
            }*/

            sendRequest(rocketRPCRequest);

           /* if(LOGGER.isDebugEnabled()){
                LOGGER.debug("client write message to send buffer,wait for response,request id: "+rocketRPCRequest.getId());
            }*/
        }catch (Exception e) {
            rcfResponse = null;
            //LOGGER.error("send request to os sendbuffer error", e);
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
            rcfResponse =new RcfResponse(rocketRPCRequest.getId(), rocketRPCRequest.getCodecType(), rocketRPCRequest.getProtocolType());
        }else if(result==null&&(System.currentTimeMillis() - beginTime)>rocketRPCRequest.getTimeout()){//结果集超时
            String errorMsg = "receive response timeout("
                    + rocketRPCRequest.getTimeout() + " ms),server is: "
                    + getServerIP() + ":" + getServerPort()
                    + " request id is:" + rocketRPCRequest.getId();
           // LOGGER.error(errorMsg);
            rcfResponse=new RcfResponse(rocketRPCRequest.getId(), rocketRPCRequest.getCodecType(), rocketRPCRequest.getProtocolType());
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
           // LOGGER.error(errorMsg, t);
            //destroy();
            //throw new Exception(errorMsg, t);
            return null;
        }

        return rcfResponse.getResponse();
    }

    /**
     * 发送请求
     * @throws Exception
     */
    public abstract void sendRequest(RcfRequest RcfRequest) throws Exception;
    /**
     * 消灭消息
     * @throws Exception
     */
    public abstract void destroy() throws Exception;
}
