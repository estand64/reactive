package com.example.reactive.webclients;

import com.example.reactive.dto.ReturnedItem;

public interface WaitWebClient {
    ReturnedItem callOne();
    ReturnedItem callTwo();
    ReturnedItem callThree();
    ReturnedItem callFour();
    ReturnedItem callFive();
}
