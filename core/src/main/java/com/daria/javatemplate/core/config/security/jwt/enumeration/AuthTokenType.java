package com.daria.javatemplate.core.config.security.jwt.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthTokenType {

    BEARER_TYPE("Bearer ");

    private final String tokenType;
}

