package ru.clevertec.nms.cache.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import ru.clevertec.nms.util.enums.CacheAlgorithm;
import ru.clevertec.nms.cache.Cache;

public class CacheFactoryImpl<K, V> extends AbstractFactoryBean<Cache<K, V>> {

    @Value("${application.cache.algorithm:LFU}")
    CacheAlgorithm algorithm;

    @Value("${application.cache.capacity:10}")
    Integer capacity;

    @Override
    public Class<?> getObjectType() {
        return Cache.class;
    }

    @Override
    protected Cache<K, V> createInstance() {
        return switch (algorithm) {
            case LFU -> new LfuCache<>(capacity);
            case LRU -> new LruCache<>(capacity);
        };
    }
}
