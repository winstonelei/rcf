package com.github.rcf.core.async;

/**
 * Created by winstone on 2017/6/22.
 */
public interface AsyncCallback<R> {

     <R> R call();

}
