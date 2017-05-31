package com.github.rcf.core.server;

/**
 * Created by winstone on 2017/5/27.
 */
public interface RcfRpcServer {

    /**
     * 注册服务
     * @param serviceName 服务名称
     * @param serviceInstance 服务实例
     */
    public void registerProcessor(String serviceName,Object serviceInstance);

    /**
     * 停止
     */
    public void stop() throws Exception;

    /**
     *
     * @param port
     * @param timeout
     * @throws Exception
     */
    public void start(int port,int timeout) throws Exception;
}
