package com.daria.javatemplate.core.service;


import com.daria.javatemplate.core.common.model.entity.CacheKeyEntity;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapperService objectMapperService;
    private final ValueOperations<String, String> valueOps;
    private final HashOperations<String, String, String> hashOps;

    public RedisCacheService(RedisTemplate<String, String> redisTemplate, ObjectMapperService objectMapperService) {
        this.redisTemplate = redisTemplate;
        this.objectMapperService = objectMapperService;
        this.valueOps = redisTemplate.opsForValue();
        this.hashOps = redisTemplate.opsForHash();
    }

    @Override
    public <T> Optional<T> get(CacheKeyEntity key, Class<T> clazz) {
        String value = valueOps.get(key.getValueKey());
        if (StringUtils.isEmpty(value)) {
            return Optional.empty();
        }

        return Optional.of(objectMapperService.getFromJson(value, clazz));
    }

    @Override
    public <T> void set(CacheKeyEntity key, T value) {
        valueOps.set(key.getValueKey(), objectMapperService.toJsonString(value));
        expire(key);
    }

    @Override
    public <T> List<T> getList(CacheKeyEntity key, Class<T> clazz) {
        String value = valueOps.get(key.getValueKey());
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return objectMapperService.getListFromJson(value, clazz);
    }

    @Override
    public void del(CacheKeyEntity key) {
        redisTemplate.delete(key.getValueKey());
    }

    @Override
    public void expire(CacheKeyEntity key) {
        long ttl = key.getType().getTtl();

        if (ttl > 0) {
            redisTemplate.expire(key.getValueKey(), ttl, TimeUnit.SECONDS);
        }
    }

    @Override
    public long decrBy(CacheKeyEntity key, long decrement) {
        Long count = valueOps.decrement(key.getValueKey(), decrement);

        return count == null ? 0 : count;
    }

    @Override
    public long incrBy(CacheKeyEntity key, long increment) {
        Long count = valueOps.increment(key.getValueKey(), increment);

        return count == null ? 0 : count;
    }

    @Override
    public boolean exist(CacheKeyEntity key) {
        return StringUtils.isNotEmpty(valueOps.get(key.getValueKey()));
    }

    @Override
    public <T> Optional<T> hGet(CacheKeyEntity key, Class<T> clazz) {
        String value = hashOps.get(key.getHashKey(), key.getHashField());
        if (StringUtils.isEmpty(value)) {
            return Optional.empty();
        }

        return Optional.ofNullable(objectMapperService.getFromJson(value, clazz));
    }

    @Override
    public Map<String, String> hGetEntire(CacheKeyEntity key) {
        Map<String, String> entries = hashOps.entries(key.getHashKey());
        if (MapUtils.isEmpty(entries)) {
            return Maps.newHashMap();
        }

        return entries;
    }

    @Override
    public <T> void hSet(CacheKeyEntity key, T value) {
        String v = objectMapperService.toJsonString(value);
        hashOps.put(key.getHashKey(), key.getHashField(), v);
        hExpire(key);
    }

    @Override
    public <T> List<T> hGetList(CacheKeyEntity key, Class<T> clazz) {
        String value = hashOps.get(key.getHashKey(), key.getHashField());
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return objectMapperService.getListFromJson(value, clazz);
    }

    @Override
    public void hExpire(CacheKeyEntity key) {
        long ttl = key.getType().getTtl();

        if (ttl > 0) {
            redisTemplate.expire(key.getHashKey(), ttl, TimeUnit.SECONDS);
        }
    }

    @Override
    public void hDelByHashKey(CacheKeyEntity key) {
        redisTemplate.delete(key.getHashKey());
    }

    @Override
    public void hDelByHashKeyAndHashField(CacheKeyEntity key) {
        hashOps.delete(key.getHashKey(), key.getHashField());
    }
}
