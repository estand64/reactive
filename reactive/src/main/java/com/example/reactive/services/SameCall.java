package com.example.reactive.services;

import com.example.reactive.dto.ReturnedItem;
import com.example.reactive.webclients.BlockingWebClient;
import com.example.reactive.webclients.ReativeWebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SameCall {
    private final ReativeWebClient reativeWebClient;
    private final BlockingWebClient blockWebClient;
    private final ExecutorService executorService;

    public SameCall(ReativeWebClient reativeWebClient, BlockingWebClient blockWebClient) {
        this.reativeWebClient = reativeWebClient;
        this.blockWebClient = blockWebClient;
        executorService = Executors.newFixedThreadPool(6);
    }

    public Iterable<String> twoSameCallsBasic(){
        var items = new ArrayList<String>();
        items.add(blockWebClient.callFive().id().toString());
        items.add(blockWebClient.callFour().id().toString());
        return items;
    }

    public Iterable<String> twoSameCallsThread(){
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

    public Iterable<String> twoSameCallsReactive(){
        var itemStream = Flux.merge(reativeWebClient.callFive(), reativeWebClient.callFour());
        return itemStream.map(ri -> ri.id().toString()).toIterable();
    }
}
