package com.github.rcf.core.loadBlance.impl;

import com.github.rcf.core.loadBlance.AbstractLoadBalance;

import java.util.List;

/**
 * @author  winstone
 */
public class RoundbinLoadBalance extends AbstractLoadBalance {
    @Override
    protected <S> S doSelect(List<S> shards, String seed) {
        // TODO
        return null;
    }
}
