package com.github.rcf.core.callback;

import com.github.rcf.core.bean.RcfConstants;
import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.serializable.RcfCodes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by winstone on 2017/6/19.
 */
public class AsyncRPCCallback {

    private static final Log LOGGER = LogFactory.getLog(AsyncRPCCallback.class);

    private RcfRequest rcfRequest;

    private RcfResponse rcfResponse;

    private Lock lock = new ReentrantLock();

    private Condition lockCondition = lock.newCondition();

    public AsyncRPCCallback(RcfRequest rcfRequest){
        this.rcfRequest = rcfRequest;
    }


    public Object start() throws InterruptedException {
      try{
            lock.lock();
            lockCondition.await(RcfConstants.SYSTEM_PROPERTY_MESSAGE_CALLBACK_TIMEOUT, TimeUnit.MILLISECONDS);
            if(this.rcfResponse!=null){
                return this.rcfResponse.getResponse();
            }else{
                LOGGER.error("出现异常...返回值为空.....");
                return null;
            }
      }finally {
          lock.unlock();
      }
    }

   public void over(RcfResponse response)throws  Exception{
       try{
           lock.lock();
           lockCondition.signal();
           this.rcfResponse = response;
           LOGGER.info("客户端接受返回结果id="+rcfResponse.getRequestId());

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

       }finally {
           lock.unlock();
       }
   }


}
