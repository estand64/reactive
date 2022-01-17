package com.example.reactive.webclients;

import com.example.reactive.BadRequestException;
import com.example.reactive.MyHttpException;
import com.example.reactive.configuration.ApiProperties;
import com.example.reactive.dto.ExtendedReturnedItem;
import com.example.reactive.dto.ReturnedItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Predicate;

@Component
public class ReactiveWebClient {
    private final WebClient webClient;

    public ReactiveWebClient(ApiProperties properties) {
        this.webClient = WebClient.create(properties.getHost());
    }

    public Mono<ReturnedItem> callOne() {
        return webClient.get().uri(ApiProperties.ONE_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }

    public Mono<ReturnedItem> callTwo() {
        return webClient.get().uri(ApiProperties.TWO_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }

    public Mono<ReturnedItem> callThree() {
        return webClient.get().uri(ApiProperties.THREE_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }

    public Mono<ReturnedItem> callFour() {
        return webClient.get().uri(ApiProperties.FOUR_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }

    public Mono<ReturnedItem> callFive() {
        return webClient.get().uri(ApiProperties.FIVE_ENDPOINT).retrieve().bodyToMono(ReturnedItem.class);
    }

    public Mono<ExtendedReturnedItem> callWithParameter(UUID inputId){
        return webClient.get()
                .uri(builder -> builder.path(ApiProperties.FOUR_ENDPOINT)
                .queryParam("id", inputId).build())
                .retrieve()
                .bodyToMono(ExtendedReturnedItem.class);
    }

    public Mono<ExtendedReturnedItem> putExample(Object someBody){
        return webClient.put()
                        .uri("putEndpoint/example")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE, "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .bodyValue(someBody)
                        .retrieve()
                        .bodyToMono(ExtendedReturnedItem.class);
    }

    //Error handling examples
    public Mono<ReturnedItem> errorStatusWithoutHandle(String endpoint){
        return webClient.get().uri(endpoint).retrieve()
                .bodyToMono(ReturnedItem.class);
    }

    public Mono<ReturnedItem> errorStatusHandleExample(String endpoint){
        return webClient.get().uri(endpoint).retrieve()
                .onStatus(HttpStatus::isError,
                        response -> switch (response.rawStatusCode()){
                            case 400 -> Mono.error(new BadRequestException("bad request made"));
                            case 401, 403 -> Mono.error(new Exception("auth error"));
                            case 404 -> Mono.error(new Exception("Maybe not an error?"));
                            case 500 -> Mono.error(new Exception("server error"));
                            default -> Mono.error(new Exception("something went wrong"));
                        })
                        .bodyToMono(ReturnedItem.class);
    }

   public Mono<ReturnedItem> nonStatusError(String host, int port, String path){
        return webClient.get().uri(uriBuilder -> uriBuilder.host(host).port(port).path(path).build()).retrieve()
                .onStatus(HttpStatus::isError,
                        response -> switch (response.rawStatusCode()){
                            case 400 -> Mono.error(new BadRequestException("bad request made"));
                            case 401, 403 -> Mono.error(new Exception("auth error"));
                            case 404 -> Mono.error(new Exception("Maybe not an error?"));
                            case 500 -> Mono.error(new Exception("server error"));
                            default -> Mono.error(new Exception("something went wrong"));
                        })
                .bodyToMono(ReturnedItem.class)
                .onErrorMap(Throwable.class, throwable -> new Exception("plain exception"));
    }

    public Mono<ExtendedReturnedItem> avoidErrorSwallow(String host, int port, String path){
        return webClient.get().uri(uriBuilder -> uriBuilder.host(host).port(port).path(path).build()).retrieve()
                .onStatus(HttpStatus::isError,
                        response -> switch (response.rawStatusCode()){
                            case 400 -> Mono.error(new BadRequestException("bad request made"));
                            case 401, 403 -> Mono.error(new Exception("auth error"));
                            case 404 -> Mono.error(new Exception("Maybe not an error?"));
                            case 500 -> Mono.error(new Exception("server error"));
                            default -> Mono.error(new Exception("something went wrong"));
                        })
                .bodyToMono(ExtendedReturnedItem.class)
                //don't overwrite the error handling above
                .onErrorMap(Predicate.not(BadRequestException.class::isInstance), otherException -> new Exception("other exception"));
    }
}
