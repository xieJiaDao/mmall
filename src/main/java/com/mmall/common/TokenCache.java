package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存 初始值1000
 * 最大 10000,超过10000 LRU清除缓存
 * 有效期是12小时
 * 如果key对应值没有，默认"null"
 * @author xieyafei
 */
@Slf4j
public class TokenCache {

    public static final String TOKEN_PREFIX = "token_";

    //超过最大值会使用LRU（最少使用算法）算法去清除缓存。
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当get取值，key没有对应的值的时候，就调用这个方法去加载。
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setkey(String key,String value){
        localCache.put(key, value);
    }

    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value))
                return null;
            return value;
        } catch (ExecutionException e) {
            log.error("LocalCache get error:",e);
        }
        return null;
    }
}
