package com.github.rcf.server.service.impl;

import com.github.rcf.registry.Config;
import com.github.rcf.registry.ZkClient;
import com.github.rcf.registry.ZookeeperTransporter;
import com.github.rcf.registry.curator.CuratorZookeeperTransporter;
import com.github.rcf.server.service.IserverService;

/**
 * Created by winstone on 2017/5/29 0029.
 */
public class ServerServiceImpl implements IserverService {

    private ZookeeperTransporter zookeeperTransporter;

    private ZkClient client;

    public static final int TYPE = 0;

    @Override
    public void close() throws Exception{
        // TODO Auto-generated method stub
        client.close();
    }

    /* (non-Javadoc)
     * @see com.cross.plateform.common.rpc.service.server.ICommonServiceServer#connectZookeeper(java.lang.String, int)
     */
    @Override
    public void connectZookeeper(String server, int timeout) throws Exception {
        Config config = new Config();
        config.setRegistryAddress("121.40.129.155:2181");
        config.setInvokeTimeoutMillis(timeout);
        zookeeperTransporter = new CuratorZookeeperTransporter();
        client = zookeeperTransporter.connect(config);
    }

    @Override
    public void registerServer(String group, String server) throws Exception{
        client.create("/" + group, group, false, true);
        client.create("/" + group + "/"+server, server,true,true);
    }

    @Override
    public void registerClient(String server, String clientInfo) throws Exception {
        client.create("/" + server+clientInfo, clientInfo, true, true);//创建临时节点
    }

    /**
     * @return the client
     */
    public ZkClient getClient() {
        return  client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(ZkClient client) {
        this.client = client;
    }

}
