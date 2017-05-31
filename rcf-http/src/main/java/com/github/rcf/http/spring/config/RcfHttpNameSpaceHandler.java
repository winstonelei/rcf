package com.github.rcf.http.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfHttpNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("service", new RcfRpcHttpServiceParser());
        registerBeanDefinitionParser("registry", new RcfHttpRegistryParser());
    }

}
