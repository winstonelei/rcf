package com.github.rcf.core.async;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

/**
 * Created by winstone on 2017/6/22.
 */
public class AsyncCallObjectInterceptor implements MethodInterceptor {

    private static final String RCFSTATUS ="_getStatus";

    private Future future;


    public AsyncCallObjectInterceptor(Future future){
        this.future = future;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if(RCFSTATUS.equalsIgnoreCase(method.getName())){
            return getStatus();
        }
        return null;
    }

    private Object getStatus(){
        long startTime = 0L;
        long endTime = 0L;
        if (future instanceof AsyncFuture) {
            startTime = ((AsyncFuture) future).getStartTime();
            endTime = ((AsyncFuture) future).getEndTime();
        }

        CallStatus status = null;

        if (future.isCancelled()) {
            status = CallStatus.TIMEOUT;
        } else if (future.isDone()) {
            status = CallStatus.DONE;
        } else {
            status = CallStatus.RUN;
            if (endTime == 0) {
                endTime = System.currentTimeMillis();
            }
        }

        return new AsyncCallStatus(startTime, (endTime - startTime), status);
    }
}
