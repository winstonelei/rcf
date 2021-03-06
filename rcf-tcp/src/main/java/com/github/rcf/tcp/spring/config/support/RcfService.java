package com.github.rcf.tcp.spring.config.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import com.github.rcf.tcp.server.RcfTcpServer;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfService implements ApplicationContextAware, ApplicationListener {


    private String interfacename;//接口名称 key

    private String ref;//服务类bean value

    private ApplicationContext applicationContext;

    public String getInterfacename() {
        return interfacename;
    }

    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getFilterRef() {
        return filterRef;
    }

    public void setFilterRef(String filterRef) {
        this.filterRef = filterRef;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private String filterRef;//拦截器类

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        RcfTcpServer.getInstance().registerProcessor(interfacename, applicationContext.getBean(ref));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext=applicationContext;
    }

}
