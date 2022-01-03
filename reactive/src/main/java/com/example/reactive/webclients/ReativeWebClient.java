package com.example.reactive.webclients;

import com.example.reactive.configuration.WaitApiProperties;
import com.example.reactive.dto.ReturnedItem;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ReativeWebClient{
    private final WaitApiProperties properties;
    private final WebClient webClient;

    public ReativeWebClient(WaitApiProperties properties) {
        this.properties = properties;
        this.webClient = WebClient.builder()
                .baseUrl(properties.getHost())
                .build();
    }

    public Mono<ReturnedItem> callOne() {
        return webClient.get().uri(WaitApiProperties.ONE_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }

    public Mono<ReturnedItem> callTwo() {
        return webClient.get().uri(WaitApiProperties.TWO_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }

    public Mono<ReturnedItem> callThree() {
        return webClient.get().uri(WaitApiProperties.THREE_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }

    public Mono<ReturnedItem> callFour() {
        return webClient.get().uri(WaitApiProperties.FOUR_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }

    public Mono<ReturnedItem> callFive() {
        return webClient.get().uri(WaitApiProperties.FIVE_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }
}
