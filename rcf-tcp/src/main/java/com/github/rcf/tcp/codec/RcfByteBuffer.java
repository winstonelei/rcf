package com.github.rcf.tcp.codec;

import com.github.rcf.core.buffer.RpcByteBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfByteBuffer implements RpcByteBuffer {

    private ByteBuf buffer;

    private ChannelHandlerContext ctx;

    public RcfByteBuffer(ByteBuf in) {
        buffer = in;
    }

    public RcfByteBuffer(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public RcfByteBuffer get(int capacity) {
        if (buffer != null)
            return this;
        buffer = ctx.alloc().buffer(capacity);
        return this;
    }

    public byte readByte() {
        return buffer.readByte();
    }

    public void readBytes(byte[] dst) {
        buffer.readBytes(dst);
    }

    public int readInt() {
        return buffer.readInt();
    }

    public int readableBytes() {
        return buffer.readableBytes();
    }

    public int readerIndex() {
        return buffer.readerIndex();
    }

    public void setReaderIndex(int index) {
        buffer.setIndex(index, buffer.writerIndex());
    }

    public void writeByte(byte data) {
        buffer.writeByte(data);
    }

    public void writeBytes(byte[] data) {
        buffer.writeBytes(data);
    }

    public void writeInt(int data) {
        buffer.writeInt(data);
    }

    public ByteBuf getBuffer() {
        return buffer;
    }

    public void writeByte(int index, byte data) {
        buffer.writeByte(data);
    }

}
