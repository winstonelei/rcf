package com.github.rcf.registry.serializable;


/**
 * @author  winstone
 *
 */
public interface ZkSerializer {

    public byte[] serialize(Object data) throws ZkMarshallingException;

    public Object deserialize(byte[] bytes) throws ZkMarshallingException;
}
