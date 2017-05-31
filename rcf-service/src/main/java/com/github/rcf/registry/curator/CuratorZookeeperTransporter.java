package com.github.rcf.registry.curator;


import com.github.rcf.registry.Config;
import com.github.rcf.registry.ZkClient;
import com.github.rcf.registry.ZookeeperTransporter;

public class CuratorZookeeperTransporter implements ZookeeperTransporter {

    public ZkClient connect(Config config) {
        return new CuratorZkClient(config);
    }

}