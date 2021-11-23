package com.example.reactive.services;

import com.example.reactive.webclients.BlockingWebClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BlockingService {
    private final ExecutorService executorService;
    private final BlockingWebClient webClient;

    public BlockingService(BlockingWebClient webClient) {
        this.webClient = webClient;
        executorService = Executors.newFixedThreadPool(6);
    }
}
