package com.example.reactive.webclients;

import com.example.reactive.BadRequestException;
import com.example.reactive.configuration.ApiProperties;
import com.example.reactive.dto.ReturnedItem;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class RetryReactiveWebClient {
    private static final String ERROR_ENDPOINT = "/randomError";
    private final WebClient webClient;

    public RetryReactiveWebClient(ApiProperties properties) {
        this.webClient = WebClient.create(properties.getHost());
    }

    //Retry examples
    public Mono<ReturnedItem> simpleRetryExample(){
        return webClient.get().uri(ERROR_ENDPOINT).retrieve()
                .bodyToMono(ReturnedItem.class)
                .log()
                .retry(3);
    }

    public Mono<ReturnedItem> backoffRetryExample(){
        return webClient.get().uri("400error").retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadRequestException("400 error")))
                .bodyToMono(ReturnedItem.class)
                .log()
                .retryWhen(Retry.backoff(3, Duration.ofMillis(2))
                        .jitter(0.5)
                        .transientErrors(true)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure()));
    }
}
