package com.github.rcf.core.loadBlance;

import java.util.List;

/**
 * @author  winstone
 */
public class RoundbinLoadBalance extends AbstractLoadBalance{
    @Override
    protected <S> S doSelect(List<S> shards, String seed) {
        // TODO
        return null;
    }
}
