package com.github.rcf.client.service;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public interface IClientService {


    /**
     * 获取服务端 group 对应的server
     * @param group
     * @return
     */
    public Set<InetSocketAddress> getServersByGroup(String group) throws Exception;

    /**
     * 关闭服务
     */
    public void close() throws Exception;

    /**
     * 连接zk
     * @param server
     * @param timeout
     * @throws Exception
     */
    public void connectZookeeper(String server,int timeout) throws Exception;

}
