package com.example.reactive.controllers;

import com.example.reactive.BadRequestException;
import com.example.reactive.MyHttpException;
import com.example.reactive.configuration.ApiProperties;
import com.example.reactive.dto.ExtendedReturnedItem;
import com.example.reactive.dto.ReturnedItem;
import com.example.reactive.webclients.ReactiveWebClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveErrorExamplesController {
    private static final String endpoint400 = "400error";
    private static final String endpoint500 = "500error";
    private final ApiProperties properties;
    private final ReactiveWebClient reactiveWebClient;

    public ReactiveErrorExamplesController(ApiProperties properties, ReactiveWebClient reactiveWebClient) {
        this.properties = properties;
        this.reactiveWebClient = reactiveWebClient;
    }

    @GetMapping("/defaultHandle")
    public Mono<ReturnedItem> defaultHandle(){
        return reactiveWebClient.errorStatusWithoutHandle(endpoint400);
    }

    @GetMapping("/basicHandle")
    public Mono<ReturnedItem> basicHandle(){
        return reactiveWebClient.errorStatusHandleExample(endpoint400);
    }

    @GetMapping("/nonStatusError")
    public Mono<ReturnedItem> nonStatusError(){
        return reactiveWebClient.nonStatusError("unknown-url", 9000, "/fakepath");
    }

    @GetMapping("/statusErrorWithExceptionMapping")
    public Mono<ExtendedReturnedItem> statusErrorWithExceptionMapping(){
        var split = properties.getHost().split("/");
        var split2 = split[split.length-1].split(":");
        return reactiveWebClient.avoidErrorSwallow(split2[0], Integer.parseInt(split2[1]), endpoint400);
    }

    //specific return exception
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> onBadRequest(BadRequestException badRequestException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> other(Exception exception){
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT.value()).body(exception.getLocalizedMessage());
    }
}
