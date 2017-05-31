package com.github.rcf.tcp.codec;

import com.github.rcf.core.protocol.rcf.RcfRpcCustomProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfEncoderHandler extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf out)
            throws Exception {
        // TODO Auto-generated method stub
        RcfByteBuffer byteBufferWrapper = new RcfByteBuffer(ctx);
        RcfRpcCustomProtocol.encode(message, byteBufferWrapper);
        ctx.write(byteBufferWrapper.getBuffer());
    }

}
