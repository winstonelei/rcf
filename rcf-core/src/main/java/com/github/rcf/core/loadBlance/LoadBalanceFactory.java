package com.github.rcf.core.loadBlance;

import com.github.rcf.core.loadBlance.impl.RandomLoadBalance;

/**
 * Created by winstone on 2017/6/1.
 */
public class LoadBalanceFactory {

    private static AbstractLoadBalance abstractLoadBalance = new RandomLoadBalance();


    public static  AbstractLoadBalance getLoadBalance(){
        return abstractLoadBalance;
    }

}
