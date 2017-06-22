package com.github.rcf.core.async;

/**
 * Created by winstone on 2017/6/22.
 */
public class AsyncCallException  extends RuntimeException {

    public AsyncCallException() {
        super();
    }

    public AsyncCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsyncCallException(String message) {
        super(message);
    }

    public AsyncCallException(Throwable cause) {
        super(cause);
    }
}
