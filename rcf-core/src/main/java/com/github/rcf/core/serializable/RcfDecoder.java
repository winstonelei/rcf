package com.github.rcf.core.serializable;

/**
 * Created by winstone on 2017/5/27.
 */
public interface RcfDecoder {

    /**
     * decode byte[] to Object
     */
    public Object decode(String className,byte[] bytes) throws Exception;
}
