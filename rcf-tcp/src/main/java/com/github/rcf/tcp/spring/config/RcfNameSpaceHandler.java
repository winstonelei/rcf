package com.github.rcf.tcp.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        // TODO Auto-generated method stub
        registerBeanDefinitionParser("reference", new RcfReferenceParser()); //定义rpc引用，主要是客户端调用时生成代理对象
        registerBeanDefinitionParser("service", new RcfServiceParser());//向内存中注册服务service
        registerBeanDefinitionParser("registry", new RcfRegistryParser());//服务端正式启动服务，并且正式注册到zk上
        registerBeanDefinitionParser("application", new RcfApplicatonParser());//服务端和客户端连接到zk上，并且启动客户端netty服务
    }
}
