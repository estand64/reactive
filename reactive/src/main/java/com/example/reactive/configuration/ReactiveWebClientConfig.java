package com.example.reactive.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ReactiveWebClientConfig {
    private final ApiProperties properties;

    public ReactiveWebClientConfig(ApiProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WebClient webClient(){
        return WebClient.builder().baseUrl(properties.getHost()).build();
    }
}
