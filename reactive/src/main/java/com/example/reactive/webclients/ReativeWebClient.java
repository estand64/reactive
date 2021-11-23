package com.example.reactive.webclients;

import com.example.reactive.configuration.WaitApiProperties;
import com.example.reactive.dto.ReturnedItem;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ReativeWebClient implements WaitWebClient{
    private final WebClient webClient;

    public ReativeWebClient(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public ReturnedItem callOne() {
        return null;
    }

    @Override
    public ReturnedItem callTwo() {
        return null;
    }

    @Override
    public ReturnedItem callThree() {
        return null;
    }

    @Override
    public ReturnedItem callFour() {
        return null;
    }

    @Override
    public ReturnedItem callFive() {
        return null;
    }
}
