package com.github.rcf.core.protocol.rcf;

import com.github.rcf.core.buffer.RcfBaseByteBuffer;
import com.github.rcf.core.protocol.RcfProtocol;

/**
 * Created by winstone on 2017/5/27.
 */
public class RcfRpcCustomProtocol {

    public static final int HEADER_LEN = 2;

    public static final byte CURRENT_VERSION = (byte)1;

    public static RcfBaseByteBuffer encode(Object message, RcfBaseByteBuffer bytebufferWrapper) throws Exception {
        RcfProtocol protocol =  RcfProtocolFactory.getProtocol();
        return protocol.encode(message, bytebufferWrapper);
    }

    public static Object decode(RcfBaseByteBuffer wrapper, Object errorObject) throws Exception {
        final int originPos = wrapper.readerIndex();
        if(wrapper.readableBytes() < 2){
            wrapper.setReaderIndex(originPos);
            return errorObject;
        }
        int version = wrapper.readByte();
        if(version == 1){
            int type = wrapper.readByte();
            RcfProtocol protocol = RcfProtocolFactory.getProtocol();
            if(protocol == null){
                throw new Exception("Unsupport protocol type: "+type);
            }
            return protocol.decode(wrapper, errorObject, new int[]{originPos});
        }else{
            throw new Exception("Unsupport protocol version: "+ version);
        }
    }
}
