package com.github.rcf.core.compiler;

/**
 * Created by winstone on 2017/6/21.
 */
public interface Compiler {

    Class<?> compile(String javaCode, ClassLoader classloader);

}
