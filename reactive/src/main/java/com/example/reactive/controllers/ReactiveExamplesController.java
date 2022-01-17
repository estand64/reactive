package com.example.reactive.controllers;

import com.example.reactive.BadRequestException;
import com.example.reactive.dto.ReturnObject;
import com.example.reactive.services.ReactiveService;
import com.example.reactive.webclients.ReactiveWebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveExamplesController {
    private final ReactiveService reactiveService;
    private final ReactiveWebClient reactiveWebClient;

    public ReactiveExamplesController(ReactiveService reactiveService, ReactiveWebClient reactiveWebClient) {
        this.reactiveService = reactiveService;
        this.reactiveWebClient = reactiveWebClient;
    }

    @GetMapping("/defaultStatus")
    public Mono<ReturnObject> defaultStatus(){
        return reactiveService.simpleCall();
    }

    @GetMapping("/annotationStatus")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<ReturnObject> annotationStatus(){
        return reactiveService.simpleCall();
    }

    @GetMapping("/responseStatus")
    public Mono<ResponseEntity<ReturnObject>> responseStatus(){
        var mono = reactiveService.simpleCall();

        return mono.map(returnObject -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add("ETag", "ab435e9c88dcfbc355fe9a56061712f45b5d60b5a74b1ae8c5a13a6b04c3f834");
            return new ResponseEntity<>(returnObject, headers, HttpStatus.ACCEPTED);
        });
    }

    //specific return exception
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> onBadRequest(BadRequestException badRequestException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> other(Exception exception){
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT.value()).build();
    }
}
