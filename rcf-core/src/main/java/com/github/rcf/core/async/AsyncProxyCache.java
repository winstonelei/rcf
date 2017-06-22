package com.github.rcf.core.async;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by winstone on 2017/6/22.
 */
public class AsyncProxyCache {

   private static Map<String,Class> cache = Maps.newConcurrentMap();

    public static Class get(String key){
        return cache.get(key);
    }


    public static void save(String key,Class proxyClass){
        if(!cache.containsKey(key)){
           synchronized (cache){
              if(!cache.containsKey(key)){
                  cache.put(key,proxyClass);
              }
           }
        }
    }

}
