package com.github.rcf.core.compiler;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.util.List;

/**
 * Created by winstone on 2017/6/21.
 */
public class ClassProxy {

    public <T> Class<T> createDynamicSubclass(Class<T> superclass) {
        Enhancer e = new Enhancer() {
            @Override
            protected void filterConstructors(Class sc, List constructors) {
            }
        };

        if (superclass.isInterface()) {
            e.setInterfaces(new Class[]{superclass});
        } else {
            e.setSuperclass(superclass);
        }

        e.setCallbackType(NoOp.class);
        Class<T> proxyClass = e.createClass();
        return proxyClass;
    }
}
