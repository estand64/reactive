package com.example.reactive.services;

import com.example.reactive.dto.ReturnObject;
import com.example.reactive.webclients.ReactiveWebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReactiveService {
    private final ReactiveWebClient reactiveWebClient;

    public ReactiveService(ReactiveWebClient reactiveWebClient) {
        this.reactiveWebClient = reactiveWebClient;
    }

    public Mono<ReturnObject> simpleCall(){
        return reactiveWebClient.callOne().map(returnedItem -> {
            var ids = List.of(returnedItem.id().toString());
            return new ReturnObject(ids, null);
        });
    }

    public Mono<ReturnObject> parameterCall(){
        var param = UUID.randomUUID();
        System.out.println("Calling with the parameter: " + param);
        return reactiveWebClient.callWithParameter(param)
                .map(extReturnedItem -> new ReturnObject(new ArrayList<>(), extReturnedItem));
    }
}
