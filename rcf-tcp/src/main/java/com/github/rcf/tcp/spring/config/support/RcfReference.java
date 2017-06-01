package com.github.rcf.tcp.spring.config.support;

import com.github.rcf.tcp.client.factory.RcfTcpClientFactory;
import com.github.rcf.tcp.client.proxy.impl.RcfClientProxy;
import com.github.rcf.client.service.api.ClientServiceApi;
import com.github.rcf.core.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfReference implements FactoryBean, DisposableBean {

    private static final Log LOGGER = LogFactory.getLog(RcfReference.class);

    /**
     * 接口名称
     */
    private String interfacename;

    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 编码类型
     */
    private int codecType;

    /**
     * 组名
     */
    private String group;

    @Override
    public void destroy() throws Exception {
        ClientServiceApi.getInstance().close();
        RcfTcpClientFactory.getInstance().stopClient();
    }


    @Override
    public Object getObject() throws Exception {
        return RcfClientProxy.getInstance().getProxyService(getObjectType(), timeout, codecType, getObjectType().getName(), group);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            if (StringUtils.isNullOrEmpty(interfacename)){
                LOGGER.warn("interfacename is null");
                return null;
            } else {
                return Thread.currentThread().getContextClassLoader().loadClass(interfacename);
            }

        } catch (ClassNotFoundException e) {
            LOGGER.error("spring 解析失败", e);
        }
        return null;
    }


    @Override
    public boolean isSingleton() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * @return the interfacename
     */
    public String getInterfacename() {
        return interfacename;
    }

    /**
     * @param interfacename the interfacename to set
     */
    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the codecType
     */
    public int getCodecType() {
        return codecType;
    }

    /**
     * @param codecType the codecType to set
     */
    public void setCodecType(int codecType) {
        this.codecType = codecType;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }


}
