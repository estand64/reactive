package com.example.reactive.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "api.wait")
public class ApiProperties {
    public static final String ONE_ENDPOINT = "wait1";
    public static final String TWO_ENDPOINT = "wait2";
    public static final String THREE_ENDPOINT = "wait3";
    public static final String FOUR_ENDPOINT = "wait4";
    public static final String FIVE_ENDPOINT = "wait5";

    private String host;
    private int timeOutSeconds;

    public ApiProperties() {
    }
}
