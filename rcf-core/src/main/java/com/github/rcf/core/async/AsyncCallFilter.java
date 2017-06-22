package com.github.rcf.core.async;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * Created by winstone on 2017/6/22.
 */
public class AsyncCallFilter implements CallbackFilter {

    public int accept(Method method){
         return AsyncCallObject.class.isAssignableFrom(method.getDeclaringClass()) ? 1:0;

    }
}
