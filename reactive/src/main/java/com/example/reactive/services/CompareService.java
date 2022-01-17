package com.example.reactive.services;

import com.example.reactive.dto.ExtendedReturnedItem;
import com.example.reactive.dto.ReturnObject;
import com.example.reactive.webclients.BlockingWebClient;
import com.example.reactive.webclients.ReactiveWebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CompareService {
    private final ReactiveWebClient reactiveWebClient;
    private final BlockingWebClient blockWebClient;
    private final ExecutorService executorService;

    public CompareService(ReactiveWebClient reactiveWebClient, BlockingWebClient blockWebClient) {
        this.reactiveWebClient = reactiveWebClient;
        this.blockWebClient = blockWebClient;
        executorService = Executors.newFixedThreadPool(6);
    }

    public Iterable<String> twoSameCallsBasic(){
        System.out.println("basic call to endpoint returning the same data structure");
        var items = new ArrayList<String>();
        items.add(blockWebClient.callFive().id().toString());
        items.add(blockWebClient.callFour().id().toString());
        return items;
    }

    public Iterable<String> twoSameCallsThread(){
        System.out.println("threaded call to endpoint returning the same data structure");
        var items = new ArrayList<String>();
        var five = executorService.submit(blockWebClient::callFive);
        var four = executorService.submit(blockWebClient::callFour);
        try {
            items.add(five.get().id().toString());
            items.add(four.get().id().toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return items;
    }

    public Mono<ReturnObject> twoSameCallsReactive(Instant start){
        System.out.println("reactive call to endpoint returning the same data structure");
        var itemStream = Flux.merge(reactiveWebClient.callFive(), reactiveWebClient.callFour());
        return itemStream.map(item -> item.id().toString()).collectList().map(
                list -> {
                    var millis = Instant.now().toEpochMilli() - start.toEpochMilli();
                    System.out.println("Time in seconds to make call: " + millis/1000.0);
                    return new ReturnObject(list, null);
                });
    }

    public Mono<ExtendedReturnedItem> sequentialCalls(){
        var fiveMono = reactiveWebClient.callFive();

        return fiveMono.doOnError(this::handleException)
                .flatMap(returnedItem -> reactiveWebClient.callWithParameter(returnedItem.id()));
    }

    private void handleException(Throwable e){
        System.out.println(e.getLocalizedMessage());
    }
}
