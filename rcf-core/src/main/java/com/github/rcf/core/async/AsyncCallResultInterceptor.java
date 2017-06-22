package com.github.rcf.core.async;

import net.sf.cglib.proxy.LazyLoader;

/**
 * Created by winstone on 2017/6/22.
 */
public class AsyncCallResultInterceptor implements LazyLoader {

    private AsyncCallResult result;

    public AsyncCallResultInterceptor(AsyncCallResult result){
        this.result = result;
    }

    public Object loadObject() throws Exception{
        return result.loadFuture();
    }
}
