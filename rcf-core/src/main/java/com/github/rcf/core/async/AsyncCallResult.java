package com.github.rcf.core.async;

import com.github.rcf.core.util.ReflectionUtils;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by winstone on 2017/6/22.
 */
public class AsyncCallResult {

    private Class returnClass;

    private Future future;

    private Long timeout;

    public AsyncCallResult(Class returnClass,Future future,Long timeout){
        this.returnClass = returnClass;
        this.future = future;
        this.timeout = timeout;
    }


    public Object loadFuture(){
        try {
            if (timeout <= 0L) {
                return future.get();
            } else {
                return future.get(timeout, TimeUnit.MILLISECONDS);
            }
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new AsyncCallException(e);
        } catch (InterruptedException e) {
            throw new AsyncCallException(e);
        } catch (Exception e) {
            throw new AsyncCallException(e);
        }
    }


    public Object getResult() {
        Class proxyClass = AsyncProxyCache.get(returnClass.getName());//实现本地cache
        if (proxyClass == null) {
            Enhancer enhancer = new Enhancer();
            if (returnClass.isInterface()) {
                enhancer.setInterfaces(new Class[]{AsyncCallObject.class, returnClass});
            } else {
                enhancer.setInterfaces(new Class[]{AsyncCallObject.class});
                enhancer.setSuperclass(returnClass);
            }
            enhancer.setCallbackFilter(new AsyncCallFilter());
            enhancer.setCallbackTypes(new Class[]{AsyncCallResultInterceptor.class, AsyncCallObjectInterceptor.class});
            proxyClass = enhancer.createClass();
            AsyncProxyCache.save(returnClass.getName(), proxyClass);//将代理对象保存到本地cache
        }

        Enhancer.registerCallbacks(proxyClass, new Callback[]{new AsyncCallResultInterceptor(this),
                new AsyncCallObjectInterceptor(future)});

        try {
            return ReflectionUtils.newInstance(proxyClass);
        } finally {
            Enhancer.registerStaticCallbacks(proxyClass, null);
        }
    }
}
