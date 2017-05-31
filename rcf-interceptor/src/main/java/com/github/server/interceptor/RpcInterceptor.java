package com.github.server.interceptor;

import java.lang.reflect.Method;

/**
 * @author winstone
 */
public interface RpcInterceptor {


    /**
     * method before
     * @param method
     * @param processor
     * @param requestObjects
     * @return
     */
    public boolean doBeforeRequest(Method method, Object processor, Object[] requestObjects);


    /**
     * method after
     * @param processor
     * @return
     */
    public boolean doAfterRequest(Object processor);

}
