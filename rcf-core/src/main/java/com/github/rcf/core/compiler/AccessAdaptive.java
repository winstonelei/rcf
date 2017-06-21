package com.github.rcf.core.compiler;

/**
 * Created by winstone on 2017/6/21.
 */
public interface AccessAdaptive {

    Object invoke(String javaCode,String method,Object[] args);

}
