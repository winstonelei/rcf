package com.github.rcf.tcp.spring.config.support;

import com.github.rcf.tcp.client.factory.RcfTcpClientFactory;
import com.github.rcf.client.service.api.ClientServiceApi;
import com.github.rcf.core.util.StringUtils;
import com.github.rcf.server.service.api.ServerServiceApi;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfApplication implements InitializingBean {


    private String address = null;

    private String clientid = null;

    /**
     * server :1
     * client :2
     */
    private Integer flag;

    private int timeout;

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        if(StringUtils.isNullOrEmpty(address)){
            throw new RuntimeException("address   can not be null or empty");
        }
        if(StringUtils.isNullOrEmpty(flag)){
            throw new RuntimeException("flag   can not be null or empty");
        }
        if(flag!=1&&flag!=2){
            throw new RuntimeException("flag only be 1 or 2");
        }

        if(flag==1){//服务端
            ServerServiceApi.getInstance().connectZookeeper(address, timeout);
        }else if(flag==2){//客户端
             ClientServiceApi.getInstance().connectZookeeper(address, timeout);
             RcfTcpClientFactory.getInstance().startClient(timeout);//客户端启动
        }

    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
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
     * @param flag the flag to set
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getClientid()
    {
        return clientid;
    }

    public void setClientid(String clientid)
    {
        this.clientid = clientid;
    }

}
