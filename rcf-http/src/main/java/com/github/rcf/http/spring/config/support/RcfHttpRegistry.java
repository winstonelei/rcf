package com.github.rcf.http.spring.config.support;

import com.github.rcf.http.server.RcfHttpServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfHttpRegistry implements InitializingBean, DisposableBean {

    private int port;//端口号

    private int timeout;


    @Override
    public void destroy() throws Exception {
        RcfHttpServer.getInstance().stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //启动服务，并且注册到注册中心zookeeper
        RcfHttpServer.getInstance().start(port, timeout);

    }


    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
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



}
