package com.example.weather.config;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {
    @Bean
    public CaffeineCacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("weatherCache", "forecastCache");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(10)));

        return cacheManager;
    }
}
