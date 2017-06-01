package com.github.rcf.core.loadBlance.impl;



import com.github.rcf.core.loadBlance.AbstractLoadBalance;
import com.github.rcf.core.util.ThreadLocalRandom;

import java.util.List;

/**
 * 一致性hash算法
 * @author  winstone
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    @Override
    protected <S> S doSelect(List<S> shards, String seed) {
        if(seed == null || seed.length() == 0){
            seed = "HASH-".concat(String.valueOf(ThreadLocalRandom.current().nextInt()));
        }
        ConsistentHashSelector<S> selector = new ConsistentHashSelector<S>(shards);
        return selector.selectForKey(seed);
    }
}
