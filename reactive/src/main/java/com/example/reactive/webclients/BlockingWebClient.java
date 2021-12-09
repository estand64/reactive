package com.example.reactive.webclients;

import com.example.reactive.configuration.WaitApiProperties;
import com.example.reactive.dto.ReturnedItem;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BlockingWebClient implements WaitWebClient{
    private final WaitApiProperties properties;
    private final RestTemplate restTemplate;

    public BlockingWebClient(WaitApiProperties properties) {
        this.properties = properties;
        restTemplate = new RestTemplate();
    }

    @Override
    public ReturnedItem callOne() {
        return makeCall(WaitApiProperties.ONE_ENDPOINT);
    }

    @Override
    public ReturnedItem callTwo() {
        return makeCall(WaitApiProperties.TWO_ENDPOINT);
    }

    @Override
    public ReturnedItem callThree() {
        return makeCall(WaitApiProperties.THREE_ENDPOINT);
    }

    @Override
    public ReturnedItem callFour() {
        return makeCall(WaitApiProperties.FOUR_ENDPOINT);
    }

    @Override
    public ReturnedItem callFive() {
        return makeCall(WaitApiProperties.FIVE_ENDPOINT);
    }

    private ReturnedItem makeCall(String endpoint){
        var item = restTemplate.getForEntity(properties.getHost()+"/"+ endpoint, ReturnedItem.class);
        return item.getBody();
    }
}
