package com.github.rcf.core.async;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by winstone on 2017/6/22.
 */
public class AsyncFuture<V> extends FutureTask<V> {

    private Thread callerThread;
    private Thread runnerThread;
    private long startTime = 0L;
    private long endTime = 0L;

    public AsyncFuture(Callable<V> callable) {
        super(callable);
        callerThread = Thread.currentThread();
    }

    protected void done() {
        endTime = System.currentTimeMillis();
    }

    public void run() {
        startTime = System.currentTimeMillis();
        runnerThread = Thread.currentThread();
        super.run();
    }

    public Thread getCallerThread() {
        return callerThread;
    }

    public Thread getRunnerThread() {
        return runnerThread;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

}
