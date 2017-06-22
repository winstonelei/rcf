package com.github.rcf.core.async;

import com.github.rcf.core.bean.RcfConstants;
import com.github.rcf.core.thread.RcfThreadPool;
import com.github.rcf.core.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by winstone on 2017/6/22.
 */
public class AsyncInvoker {

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) RcfThreadPool.getExecutor(RcfConstants.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS, RcfConstants.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS);

    public <R> R submit(AsyncCallback callback){
       Type type = callback.getClass().getGenericInterfaces()[0];
        if(type instanceof ParameterizedType){
            Class returnClass = (Class) ReflectionUtils.getGenericClass((ParameterizedType) type,0);
            return (R) intercept(callback,returnClass);
        }else{
            throw new AsyncCallException("RcfRpc AsyncCallback must be parameterized type!");
        }
    }


    private <R> R intercept(AsyncCallback<R> callback,Class<?> returnClass){
        if (!Modifier.isPublic(returnClass.getModifiers())) {
            return  callback.call();
        } else if (Modifier.isFinal(returnClass.getModifiers())) {
            return callback.call();
        } else if (Void.TYPE.isAssignableFrom(returnClass)) {
            return callback.call();
        } else if (returnClass.isPrimitive() || returnClass.isArray()) {
            return  callback.call();
        } else if (returnClass == Object.class) {
            return  callback.call();
        } else {
            return submit(callback, returnClass);
        }

    }


    private <T> AsyncFuture<T> submit(Callable<T> task){
        AsyncFuture future = new AsyncFuture(task);
        executor.submit(future);
        return future;
    }

    private <R> R submit(AsyncCallback<R> asyncCallback,Class<?> returnClass){
        Future future = submit(new Callable() {
            @Override
            public R call() throws Exception {
                return asyncCallback.call();
            }
        });

        AsyncCallResult result = new AsyncCallResult(returnClass, future, RcfConstants.SYSTEM_PROPERTY_ASYNC_MESSAGE_CALLBACK_TIMEOUT);
        R asyncProxy = (R) result.getResult();

        return asyncProxy;
    }




}
