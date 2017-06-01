package com.github.rcf.core.bean;

import com.github.rcf.core.serializable.RcfCodes;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by winstone on 2017/5/27.
 */
public class RcfRequest implements Serializable {

    private static final long serialVersionUID = -3554311529871950375L;

    private byte[] targetInstanceName;

    private byte[] methodName;

    private byte[][] argTypes;

    public void setArgTypes(byte[][] argTypes) {
        this.argTypes = argTypes;
    }

    public byte[][] getArgTypes() {
        return argTypes;
    }


    private Object[] requestObjects = null;

    private Object message = null;

    private int timeout = 0;

    private int id ;

    private int codecType = RcfCodes.JAVA_CODEC;

    private int messageLen;

    private static final AtomicInteger requestIdSeq = new AtomicInteger();

    public RcfRequest(byte[] targetInstanceName,byte[] methodName,byte[][] argTypes,
                            Object[] requestObjects,int timeout,int codecType){
        this(targetInstanceName,methodName,argTypes,requestObjects,timeout,get(),codecType);
    }

    public RcfRequest(byte[] targetInstanceName,byte[] methodName,byte[][] argTypes,
                            Object[] requestObjects,int timeout,int id,int codecType){
        this.requestObjects = requestObjects;
        this.id = id;
        this.timeout = timeout;
        this.targetInstanceName = targetInstanceName;
        this.methodName = methodName;
        this.argTypes = argTypes;
        this.codecType = codecType;
    }



    public int getMessageLen() {
        return messageLen;
    }

    public void setMessageLen(int messageLen) {
        this.messageLen = messageLen;
    }



    public int getCodecType() {
        return codecType;
    }

    public Object getMessage() {
        return message;
    }

    public byte[] getTargetInstanceName() {
        return targetInstanceName;
    }

    public byte[] getMethodName() {
        return methodName;
    }

    public int getTimeout() {
        return timeout;
    }

    public Object[] getRequestObjects() {
        return requestObjects;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }



    public static Integer get(){

        return requestIdSeq.incrementAndGet();
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}
