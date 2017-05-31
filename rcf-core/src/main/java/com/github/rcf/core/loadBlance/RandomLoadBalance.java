package com.github.rcf.core.loadBlance;



import com.github.rcf.core.util.ThreadLocalRandom;

import java.util.List;

/**
 * 随机负载均衡算法
 * @author  winstone
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected <S> S doSelect(List<S> shards, String seed) {
        return shards.get(ThreadLocalRandom.current().nextInt(shards.size()));
    }
}
