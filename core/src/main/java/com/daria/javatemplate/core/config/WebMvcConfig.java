package com.daria.javatemplate.core.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Autowired(required = false)
    private List<HandlerInterceptor> handlerInterceptors = Lists.newArrayList();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        handlerInterceptors.forEach(handlerInterceptor -> registry.addInterceptor(handlerInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/images/**"));
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return sessionLocaleResolver;
    }
}
