package com.github.rcf.core.async;

/**
 * Created by winstone on 2017/6/22.
 */
public class AsyncCallStatus {

    private long startTime;

    private long elapseTime;

    private CallStatus status;

    public AsyncCallStatus(long startTime,long elapseTime,CallStatus status){
        this.startTime = startTime;
        this.elapseTime = elapseTime;
        this.status = status;
    }


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(long elapseTime) {
        this.elapseTime = elapseTime;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public String toString() {
        return "AsyncLoadStatus [status=" + status + ", startTime=" + startTime + ", elapseTime=" + elapseTime + "]";
    }

}
