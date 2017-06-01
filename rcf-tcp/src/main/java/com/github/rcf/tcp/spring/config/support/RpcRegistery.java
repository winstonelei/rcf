package com.github.rcf.tcp.spring.config.support;

import com.github.rcf.core.util.StringUtils;
import com.github.rcf.server.service.api.ServerServiceApi;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import com.github.rcf.tcp.server.RcfTcpServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RpcRegistery implements InitializingBean, DisposableBean {

    private String ip;//暴露的ip

    private int port;//端口号

    private int timeout;

    private int codecType;//编码类型

    private String group;//组

    private int threadCount;//线程数
    @Override
    public void destroy() throws Exception {
        ServerServiceApi.getInstance().close();
        RcfTcpServer.getInstance().stop();//停止
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(port==0){
            throw new Exception("parameter port can not be null");
        }
        ServerServiceApi.getInstance().registerService(group, getLocalhost());
        RcfTcpServer.getInstance().setCodecType(codecType);
        RcfTcpServer.getInstance().setThreadCount(threadCount);

        RcfTcpServer.getInstance().start(port,timeout);

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

    public int getTimeout() {
        return timeout;
    }

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

    private String getLocalhost(){
        try {
            String ip = StringUtils.isNullOrEmpty(this.getIp()) ? InetAddress.getLocalHost().getHostAddress() : this.getIp();
            return ip+":"+port;
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("无法获取本地Ip",e);
        }

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

    /**
     * @return the threadCount
     */
    public int getThreadCount() {
        return threadCount;
    }

    /**
     * @param threadCount the threadCount to set
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }
}
