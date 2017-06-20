package com.github.rcf.core.thread.policy;

/**
 * Created by winstone on 2017/6/20.
 */
public interface RejectedRunnable extends Runnable {
    void rejected();
}