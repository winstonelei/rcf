package com.github.rcf.registry;



public interface ZookeeperTransporter {

    ZkClient connect(Config config);

}
