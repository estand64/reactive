package com.example.reactive.webclients;

import com.example.reactive.configuration.ApiProperties;
import com.example.reactive.dto.ReturnedItem;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BlockingWebClient{
    private final ApiProperties properties;
    private final RestTemplate restTemplate;

    public BlockingWebClient(ApiProperties properties) {
        this.properties = properties;
        restTemplate = new RestTemplate();
    }

    public ReturnedItem callOne() {
        return makeCall(ApiProperties.ONE_ENDPOINT);
    }

    public ReturnedItem callTwo() {
        return makeCall(ApiProperties.TWO_ENDPOINT);
    }

    public ReturnedItem callThree() {
        return makeCall(ApiProperties.THREE_ENDPOINT);
    }

    public ReturnedItem callFour() {
        return makeCall(ApiProperties.FOUR_ENDPOINT);
    }

    public ReturnedItem callFive() {
        return makeCall(ApiProperties.FIVE_ENDPOINT);
    }

    private ReturnedItem makeCall(String endpoint){
        var item = restTemplate.getForEntity(properties.getHost()+"/"+ endpoint, ReturnedItem.class);
        return item.getBody();
    }
}
