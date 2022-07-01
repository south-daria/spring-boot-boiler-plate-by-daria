package com.daria.javatemplate.core.service;

import com.daria.javatemplate.core.common.model.entity.CacheKeyEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

interface CacheService {
    /**
     * Value Operation
     */
    default <T> T get(CacheKeyEntity key, Supplier<T> supplier, Class<T> clazz) {
        return get(key, clazz).orElseGet(() -> set(key, supplier));
    }

    <T> Optional<T> get(CacheKeyEntity key, Class<T> clazz);

    default <T> T set(CacheKeyEntity key, Supplier<T> supplier) {
        T data = supplier.get();

        if (data != null) {
            set(key, data);
        }

        return data;
    }

    <T> void set(CacheKeyEntity key, T value);

    default <T> List<T> getList(CacheKeyEntity key, Supplier<List<T>> supplier, Class<T> clazz) {
        List<T> data = getList(key, clazz);

        if (data == null) {
            List<T> listData = supplier.get();
            set(key, listData);
            return listData;
        }

        return data;
    }

    default <T> List<T> pushList(CacheKeyEntity key, Supplier<List<T>> supplier, Class<T> clazz) {
        List<T> data = getList(key, clazz);
        List<T> listData = supplier.get();

        if (data == null) {
            set(key, listData);
            return listData;
        }

        data.addAll(listData);
        set(key, data);

        return data;
    }

    <T> List<T> getList(CacheKeyEntity key, Class<T> clazz);

    void del(CacheKeyEntity key);

    void expire(CacheKeyEntity key);

    long decrBy(CacheKeyEntity key, long decrement);

    default long decrBy(CacheKeyEntity key, Supplier<Long> supplier, long decrement) {
        if (!exist(key)) {
            Long data = supplier.get();

            if (data != null) {
                incrBy(key, data);
                expire(key);
            }
        }

        return decrBy(key, decrement);
    }

    long incrBy(CacheKeyEntity key, long increment);

    default long incrBy(CacheKeyEntity key, Supplier<Long> supplier, long increment) {
        if (!exist(key)) {
            Long data = supplier.get();

            if (data != null) {
                incrBy(key, data);
                expire(key);
            }
        }

        return incrBy(key, increment);
    }

    boolean exist(CacheKeyEntity key);

    /**
     * Hash Operation
     */
    default <T> T hGet(CacheKeyEntity key, Supplier<T> supplier, Class<T> clazz) {
        return hGet(key, clazz).orElseGet(() -> hSet(key, supplier));
    }

    <T> Optional<T> hGet(CacheKeyEntity key, Class<T> clazz);

    Map<String, String> hGetEntire(CacheKeyEntity key);

    default <T> T hSet(CacheKeyEntity key, Supplier<T> supplier) {
        T data = supplier.get();

        if (data != null) {
            hSet(key, data);
        }

        return data;
    }

    <T> void hSet(CacheKeyEntity key, T value);

    default <T> List<T> hGetList(CacheKeyEntity key, Supplier<List<T>> supplier, Class<T> clazz) {
        List<T> data = hGetList(key, clazz);

        if (data == null) {
            List<T> listData = supplier.get();
            hSet(key, listData);
            return listData;
        }

        return data;
    }

    <T> List<T> hGetList(CacheKeyEntity key, Class<T> clazz);

    void hExpire(CacheKeyEntity key);

    void hDelByHashKey(CacheKeyEntity key);

    void hDelByHashKeyAndHashField(CacheKeyEntity key);
}
