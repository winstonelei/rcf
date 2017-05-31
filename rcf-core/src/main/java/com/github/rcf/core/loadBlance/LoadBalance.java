package com.github.rcf.core.loadBlance;


import java.util.List;

/**
 *@author  winstone
 */
public interface LoadBalance {

    public <S> S select(List<S> shards, String seed);

}
