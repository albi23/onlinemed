package com.onlinemed.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CaffeineCacheConfig {


    @Bean(name = "caffeine-manager")
    public CacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("person");
        caffeineCacheManager.setCaffeine(caffeineCacheBuilder());
        return caffeineCacheManager;
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(32)
                .maximumSize(512)
                .expireAfterAccess(15, TimeUnit.MINUTES)
                ;
    }

}
