package com.github.rcf.core.serializable;

/**
 * Created by winstone on 2017/5/27.
 */
public interface RcfEncoder {

    /**
     * 编码
     * @param object
     * @return
     * @throws Exception
     */
    public byte[] encode(Object object) throws Exception;


}
