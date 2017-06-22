package com.github.rcf.core.protocol;

import com.github.rcf.core.buffer.RcfBaseByteBuffer;

/**
 * Created by winstone on 2017/5/27.
 */
public interface RcfProtocol {
    /**
     * 编码
     * @param message
     * @param bytebufferWrapper
     * @return
     * @throws Exception
     */
    public RcfBaseByteBuffer encode(Object message, RcfBaseByteBuffer bytebufferWrapper) throws Exception;

    /**
     * 解码
     * @param wrapper
     * @param errorObject
     * @param originPos
     * @return
     * @throws Exception
     */
    public Object decode(RcfBaseByteBuffer wrapper, Object errorObject, int...originPos) throws Exception;

}
