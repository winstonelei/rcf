package com.github.rcf.core.protocol.rcf;

import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;
import com.github.rcf.core.buffer.RpcByteBuffer;
import com.github.rcf.core.protocol.RcfProtocol;

/**
 * Created by winstone on 2017/5/27.
 */
public class RcfRpcCustomProtocol {

    public static final int HEADER_LEN = 2;

    public static final byte CURRENT_VERSION = (byte)1;

    public static RpcByteBuffer encode(Object message,RpcByteBuffer bytebufferWrapper) throws Exception {
        Integer type = 0;
        if(message instanceof RcfRequest){
            type = ((RcfRequest)message).getProtocolType();
        }else if(message instanceof RcfResponse){
            type = ((RcfResponse)message).getProtocolType();
        }
        RcfProtocol protocol =  RcfProtocolFactory.getProtocol(type);
        return protocol.encode(message, bytebufferWrapper);
    }

    public static Object decode(RpcByteBuffer wrapper, Object errorObject) throws Exception {
        final int originPos = wrapper.readerIndex();
        if(wrapper.readableBytes() < 2){
            wrapper.setReaderIndex(originPos);
            return errorObject;
        }
        int version = wrapper.readByte();
        if(version == 1){
            int type = wrapper.readByte();
            RcfProtocol protocol = RcfProtocolFactory.getProtocol(type);
            if(protocol == null){
                throw new Exception("Unsupport protocol type: "+type);
            }
            return protocol.decode(wrapper, errorObject, new int[]{originPos});
        }else{
            throw new Exception("Unsupport protocol version: "+ version);
        }
    }
}
