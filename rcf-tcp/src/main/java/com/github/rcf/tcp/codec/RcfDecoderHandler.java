package com.github.rcf.tcp.codec;

import com.github.rcf.core.protocol.rcf.RcfRpcCustomProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfDecoderHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf,
                          List<Object> out) throws Exception {
       RcfByteBuffer wrapper = new RcfByteBuffer(buf);
        Object result = RcfRpcCustomProtocol.decode(wrapper, null);
        if (result != null) {
            out.add(result);
        }
    }

}
