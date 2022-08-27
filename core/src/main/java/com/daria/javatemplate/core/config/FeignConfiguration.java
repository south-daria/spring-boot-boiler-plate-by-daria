package com.daria.javatemplate.core.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

@RequiredArgsConstructor
public class FeignConfiguration {

    @Value("${feign-client.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            requestTemplate.header("Authorization", apiKey);
        };
    }
}