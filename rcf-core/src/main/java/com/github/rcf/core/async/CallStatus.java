package com.github.rcf.core.async;

/**
 * Created by winstone on 2017/6/22.
 */
public enum CallStatus {
    RUN,
    TIMEOUT,
    DONE;

    public boolean isRun(){ return this == RUN; }

    public boolean isTimeout(){ return this == TIMEOUT ;}

    public boolean isDone(){ return this == DONE;}
}
