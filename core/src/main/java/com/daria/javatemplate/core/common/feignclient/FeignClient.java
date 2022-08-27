package com.daria.javatemplate.core.common.feignclient;

import com.daria.javatemplate.core.config.FeignConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.cloud.openfeign.FeignClient(name="example", url="${feign-client.host}", configuration = { FeignConfiguration.class })
public interface FeignClient {
//    build.gradle implementation 'org.springframework.cloud:spring-cloud-starter-openfeign' 필요

    @GetMapping("${feign-client.path.example}")
    void example(@RequestParam(name = "query") String query);
}
