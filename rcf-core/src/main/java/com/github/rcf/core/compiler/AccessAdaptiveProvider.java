package com.github.rcf.core.compiler;

import java.io.File;

import com.github.rcf.core.util.ReflectionUtils;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

/**
 * Created by winstone on 2017/6/21.
 */
public class AccessAdaptiveProvider  extends AbstractAccessAdaptive implements AccessAdaptive {


    @Override
    protected Class<?> doCompile(String clsName, String javaSource) throws Throwable {
        NativeCompiler compiler = null;
        try {
            File tempLocation = Files.createTempDir();
            compiler = new NativeCompiler(tempLocation);
            Class type = compiler.compile(clsName, javaSource);
            return type;
        } finally {
            compiler.close();
        }
    }

    @Override
    public Object invoke(String javaCode, String method, Object[] args) {
        if (StringUtils.isEmpty(javaCode) || StringUtils.isEmpty(method)) {
            return null;
        } else {
            try {
                ClassProxy objectProxy = new ClassProxy();
                Class type = compile(javaCode, null);
                Class<?> objectClass = objectProxy.createDynamicSubclass(type);
                Object object = ReflectionUtils.newInstance(objectClass);
                return MethodUtils.invokeMethod(object, method, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
          return null;
        }
    }
}
