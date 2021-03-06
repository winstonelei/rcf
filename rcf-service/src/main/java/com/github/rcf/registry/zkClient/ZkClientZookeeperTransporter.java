package com.github.rcf.registry.zkClient;


import com.github.rcf.registry.Config;
import com.github.rcf.registry.ZkClient;
import com.github.rcf.registry.ZookeeperTransporter;

public class ZkClientZookeeperTransporter implements ZookeeperTransporter {

    public ZkClient connect(Config config) {
        return new ZkClientZkClient(config);
    }

}
