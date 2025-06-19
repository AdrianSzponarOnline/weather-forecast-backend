package com.example.weather.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestConfig {
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder,
                              @Value("${weather.api.connect-timeout:4}") long connectTimeout,
                              @Value("${weather.api.read-timeout:4}") long readTimeout) {
        return builder
                .connectTimeout(Duration.ofSeconds(connectTimeout))
                .readTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }
}
