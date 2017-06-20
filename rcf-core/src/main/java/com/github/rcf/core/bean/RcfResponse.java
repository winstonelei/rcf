package com.github.rcf.core.bean;

import com.github.rcf.core.serializable.RcfCodes;

import java.io.Serializable;

/**
 * Created by winstone on 2017/5/27.
 */
public class RcfResponse implements Serializable {

    private static final long serialVersionUID = 4590846966635080090L;

    private int requestId ;
    private Object response = null;

    private boolean isError = false;

    private Throwable exception = null;



    private int codecType = RcfCodes.JAVA_CODEC;

    private int messageLen;

    private byte[] responseClassName;

    public RcfResponse(){}

    public RcfResponse(int requestId,int codecType){
        this.requestId = requestId;
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

    public int getRequestId() {
        return requestId;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public boolean isError() {
        return isError;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
        isError = true;
    }

    public byte[] getResponseClassName() {
        return responseClassName;
    }

    public void setResponseClassName(byte[] responseClassName) {
        this.responseClassName = responseClassName;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setCodecType(int codecType) {
        this.codecType = codecType;
    }
}
