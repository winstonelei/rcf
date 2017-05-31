package com.github.rcf.registry;

/**
 * @author winstone
 *
 */
public interface DataListener {

    void dataChange(String dataPath, Object data) throws Exception;

    void dataDeleted(String dataPath) throws Exception;
}
