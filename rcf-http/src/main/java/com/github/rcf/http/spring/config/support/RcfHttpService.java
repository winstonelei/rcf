package com.github.rcf.http.spring.config.support;

import com.github.rcf.core.bean.RcfHttpBean;
import com.github.rcf.http.server.RcfHttpServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfHttpService implements ApplicationContextAware, ApplicationListener {

    private String projectname;

    private String ref;//服务类bean value

    private String filterRef;//拦截器类

    /**
     * POST,GET,HEAD,PUT,DELETE
     */
    private String httpType;

    /**
     * Type:
     * html,json,xml
     */
    private String returnType;

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // TODO Auto-generated method stub
        Object object=applicationContext.getBean(ref);
        RcfHttpBean rpcHttpBean=new RcfHttpBean(object, httpType, returnType);
        RcfHttpServer.getInstance().registerProcessor(projectname, rpcHttpBean);//注册http服务

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        // TODO Auto-generated method stub
        this.applicationContext=applicationContext;
    }

    /**
     * @return the projectname
     */
    public String getProjectname() {
        return projectname;
    }

    /**
     * @param projectname the projectname to set
     */
    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    /**
     * @return the ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * @return the filterRef
     */
    public String getFilterRef() {
        return filterRef;
    }

    /**
     * @param filterRef the filterRef to set
     */
    public void setFilterRef(String filterRef) {
        this.filterRef = filterRef;
    }

    /**
     * @return the httpType
     */
    public String getHttpType() {
        return httpType;
    }

    /**
     * @param httpType the httpType to set
     */
    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    /**
     * @return the returnType
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * @param returnType the returnType to set
     */
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    /**
     * @return the applicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
