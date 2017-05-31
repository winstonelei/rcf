package com.github.rcf.registry.curator;

import com.github.rcf.client.service.impl.ClientServiceImpl;
import com.github.rcf.registry.*;
import com.github.rcf.registry.serializable.SerializableSerializer;
import com.github.rcf.registry.serializable.ZkSerializer;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author winstone
 */
public class CuratorZkClient extends AbstractZkClient<CuratorZkClient.PathChildrenListener, CuratorZkClient.NodeListener> {

    private final CuratorFramework client;
    private final ZkSerializer zkSerializer;

    public CuratorZkClient(Config config) {
        String registryAddress = config.getRegistryAddress();
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(registryAddress)
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
                .connectionTimeoutMs(5000);

        client = builder.build();

        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework client, ConnectionState state) {
                if (state == ConnectionState.LOST) {
                    CuratorZkClient.this.stateChanged(StateListener.DISCONNECTED);
                } else if (state == ConnectionState.CONNECTED) {
                    CuratorZkClient.this.stateChanged(StateListener.CONNECTED);
                } else if (state == ConnectionState.RECONNECTED) {
                    CuratorZkClient.this.stateChanged(StateListener.RECONNECTED);
                } else if (state == ConnectionState.SUSPENDED) {
                    CuratorZkClient.this.stateChanged(StateListener.DISCONNECTED);
                }
            }
        });

        zkSerializer = new SerializableSerializer();

        client.start();
    }

    @Override
    protected String createPersistent(String path, boolean sequential) {
        try {
            if (sequential) {
                return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path);
            } else {
                return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
            }
        } catch (KeeperException.NodeExistsException e) {
            return path;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    protected String createPersistent(String path, String data, boolean sequential) {
        try {
            if (sequential) {
                return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, data.getBytes(Charsets.UTF_8));
            } else {
                return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
            }
        } catch (KeeperException.NodeExistsException e) {
            return path;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    protected String createEphemeral(String path, boolean sequential) {
        try {
            if (sequential) {
                return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path);
            } else {
                return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            }
        } catch (KeeperException.NodeExistsException e) {
            return path;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    protected String createEphemeral(String path, String data, boolean sequential) {
        try {
            if (sequential) {
                return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, data.getBytes(Charsets.UTF_8));
            } else {
                return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            }
        } catch (KeeperException.NodeExistsException e) {
            return path;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    protected PathChildrenListener createTargetChildListener(final String path, final ChildListener listener) {
        return new PathChildrenListener(path, listener);
    }

    protected List<String> addTargetChildListener(String path, PathChildrenListener listener) {
        try {
            listener.startListener();
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    protected void removeTargetChildListener(String path, PathChildrenListener listener) {
        listener.stopListener();
    }

    @Override
    protected void addTargetDataListener(String path, NodeListener listener) {
        listener.startListener();
    }

    @Override
    protected NodeListener createTargetDataListener(String path, final DataListener listener) {
        return new NodeListener(path, listener);
    }

    @Override
    protected void removeTargetDataListener(String path, NodeListener listener) {
        listener.stopListener();
    }

    @Override
    public boolean delete(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat != null) {
               client.delete().deletingChildrenIfNeeded().forPath(path);
                return true;
            }
        }catch (Exception e) {
            return  false;
            //LOGGER.error("deleteNode fail", e);
        }
        return true;
    }

    @Override
    public boolean exists(String path) {
        try {
            return client.checkExists().forPath(path) != null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getData(String path) {
        try {
            return (T) zkSerializer.deserialize(client.getData().forPath(path));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void setData(String path, Object data) {
        byte[] zkDataBytes;
        if (data instanceof Serializable) {
            zkDataBytes = zkSerializer.serialize(data);
        } else {
            zkDataBytes = (byte[]) data;
        }
        try {
            client.setData().forPath(path, zkDataBytes);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * 找到指定节点下所有子节点的名称与值
     *
     * @param node
     * @return
     */
    public Map<String, String> listChildrenDetail(String node) {
        Map<String, String> map = Maps.newHashMap();
        try {
            GetChildrenBuilder childrenBuilder = client.getChildren();
            List<String> children = childrenBuilder.forPath(node);
            GetDataBuilder dataBuilder = client.getData();
            if (children != null) {
                for (String child : children) {
                    String propPath = ZKPaths.makePath(node, child);
                    map.put(child, new String(dataBuilder.forPath(propPath), Charsets.UTF_8));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }

    @Override
    protected void doClose() {
        client.close();
    }


    //增加childrenListener
    public void addChildrenListener(final String group, ExecutorService pool,  ClientServiceImpl commonServiceClient) throws Exception {
        final ClientServiceImpl commonClient = commonServiceClient;
        PathChildrenCache childrenCache = new PathChildrenCache(client,"/"+group, true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                            throws Exception {
                        if(event.getType()== PathChildrenCacheEvent.Type.CHILD_REMOVED){//监听子节点被删除的情况
                            String path=event.getData().getPath();
                            String[] nodes=path.split("/");// 1:group 2:address
                            if(nodes.length>0&&nodes.length==3){
                                commonClient.updateServerList(nodes[1], nodes[2]);
                            }

                        }else if(event.getType()==PathChildrenCacheEvent.Type.CHILD_ADDED){//监听增加
                            String path=event.getData().getPath();
                            String[] nodes=path.split("/");// 1:group 2:address
                            if(nodes.length>0&&nodes.length==3){
                                Map<String, String> valueMap=listChildrenDetail("/"+nodes[1]);
                                for(String value:valueMap.values()){
                                    String[] nodes1=value.split(":");
                                    if(!nodes1[1].matches("\\d*")){
                                        continue;
                                    }
                                    InetSocketAddress socketAddress = new InetSocketAddress(nodes1[0], Integer.parseInt(nodes1[1]));
                                    Set<InetSocketAddress> addresses = ClientServiceImpl.servers.get(group);
                                    addresses.add(socketAddress);
                                    ClientServiceImpl.servers.put(group, addresses);
                                }

                            }
                        }
                    }
                },
                pool
        );
    }


    public class PathChildrenListener {
        private PathChildrenCache childrenCache;
        private PathChildrenCacheListener childrenCacheListener;
        private AtomicBoolean start = new AtomicBoolean(false);

        public PathChildrenListener(String path, final ChildListener listener) {
            childrenCache = new PathChildrenCache(client, path, true);
            childrenCacheListener = new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework c, PathChildrenCacheEvent event)
                        throws Exception {

                    switch (event.getType()) {
                        case CHILD_ADDED:
                        case CHILD_REMOVED:
                        case CHILD_UPDATED:
                            String childPath = event.getData().getPath();
                            String parentPath = childPath.substring(0, childPath.lastIndexOf("/"));
                            List<String> children = client.getChildren().forPath(parentPath);
                            listener.childChanged(parentPath, children);
                        default:
                            break;
                    }
                }
            };
        }

        public void startListener() {
            try {
                if (start.compareAndSet(false, true)) {
                    childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
                    childrenCache.getListenable().addListener(childrenCacheListener);
                }
            } catch (Exception e) {
                throw new ZkException(e);
            }
        }

        public void stopListener() {
            try {
                if (start.compareAndSet(true, false)) {
                    childrenCache.getListenable().removeListener(childrenCacheListener);
                    childrenCache.clear();
                    childrenCache.close();
                }
            } catch (IOException e) {
                throw new ZkException(e);
            }
        }
    }

    public class NodeListener {
        private NodeCache nodeCache;
        private NodeCacheListener nodeCacheListener;
        private AtomicBoolean start = new AtomicBoolean(false);

        public NodeListener(String path, final DataListener listener) {
            nodeCache = new NodeCache(client, path, false);
            nodeCacheListener = new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    String path = nodeCache.getCurrentData().getPath();

                    Object data = nodeCache.getCurrentData().getData();

                    if (data == null) {
                        listener.dataDeleted(path);
                    } else {
                        listener.dataChange(path, data);
                    }
                }
            };
        }

        public void startListener() {
            try {
                if (start.compareAndSet(false, true)) {
                    nodeCache.start(true);
                    nodeCache.getListenable().addListener(nodeCacheListener);
                }
            } catch (Exception e) {
                throw new ZkException(e);
            }
        }

        public void stopListener() {
            try {
                if (start.compareAndSet(true, false)) {
                    nodeCache.getListenable().removeListener(nodeCacheListener);
                    nodeCache.close();
                }
            } catch (IOException e) {
                throw new ZkException(e);
            }
        }
    }
}
