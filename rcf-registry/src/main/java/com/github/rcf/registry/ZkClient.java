package com.github.rcf.registry;



import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author winstone
 *
 */
public interface ZkClient {

    String create(String path, boolean ephemeral, boolean sequential);

    String create(String path, String data, boolean ephemeral, boolean sequential);

    boolean delete(String path);

    boolean exists(String path);

    <T> T getData(String path);

    void setData(String path, Object data);

    List<String> getChildren(String path);

    List<String> addChildListener(String path, ChildListener listener);

    void removeChildListener(String path, ChildListener listener);

    void addDataListener(String path, DataListener listener);

    void removeDataListener(String path, DataListener listener);

    void addStateListener(StateListener listener);

    void removeStateListener(StateListener listener);

    boolean isConnected();

    void close();


   //public void addChildrenListener(final String group, ExecutorService pool, ClientServiceImpl commonServiceClient) throws Exception;


    public Map<String, String> listChildrenDetail(String node);



}
