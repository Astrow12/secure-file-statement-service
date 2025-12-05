package com.test.statementservice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HeaderInterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(final InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(headerInterceptor(userStore())).excludePathPatterns("/swagger-ui/**");
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserStore userStore() {
        return new UserStore();
    }

    @Bean
    public HeaderInterceptor headerInterceptor(final UserStore userStore) {
        return new HeaderInterceptor(userStore);
    }
}
