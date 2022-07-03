package com.daria.javatemplate.core.common.model.type;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public enum CacheType {
    User("user", 5, TimeUnit.MINUTES, "User ID");

    private final String name;
    private final long ttl;
    private final TimeUnit timeUnit;
    private final String desc;

    CacheType(String name, int ttl, TimeUnit timeUnit, String desc) {
        this.name = name;
        this.ttl = TimeUnit.SECONDS.convert(ttl, timeUnit);
        this.timeUnit = timeUnit;
        this.desc = desc;
    }
}
