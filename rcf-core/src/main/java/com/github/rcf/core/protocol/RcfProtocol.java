package com.github.rcf.core.protocol;

import com.github.rcf.core.buffer.RpcByteBuffer;

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
    public RpcByteBuffer encode(Object message, RpcByteBuffer bytebufferWrapper) throws Exception;

    /**
     * 解码
     * @param wrapper
     * @param errorObject
     * @param originPos
     * @return
     * @throws Exception
     */
    public Object decode(RpcByteBuffer wrapper, Object errorObject,int...originPos) throws Exception;

}
