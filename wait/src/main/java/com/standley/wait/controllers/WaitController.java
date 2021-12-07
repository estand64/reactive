package com.standley.wait.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class WaitController {
    private static final String BAD_TEMPLATE = "Didn't sleep %n second cause something went wrong";
    private static final String GOOD_TEMPLATE = "slept %n second";

    @GetMapping("/wait1")
    public Data waitOne(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return new Data(UUID.randomUUID(), String.format(BAD_TEMPLATE, 1));
        }
        return new Data(UUID.randomUUID(), String.format(GOOD_TEMPLATE, 1));
    }

    @GetMapping("/wait2")
    public Data waitTwo(){
        long time = 2;
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            return new Data(UUID.randomUUID(), String.format(BAD_TEMPLATE, time));
        }
        return new Data(UUID.randomUUID(), String.format(GOOD_TEMPLATE, time));
    }

    @GetMapping("/wait3")
    public Data waitThree(){
        long time = 3;
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            return new Data(UUID.randomUUID(), String.format(BAD_TEMPLATE, time));
        }
        return new Data(UUID.randomUUID(), String.format(GOOD_TEMPLATE, time));
    }

    @GetMapping("/wait4")
    public Data waitFour(){
        long time = 4;
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            return new Data(UUID.randomUUID(), String.format(BAD_TEMPLATE, time));
        }
        return new Data(UUID.randomUUID(), String.format(GOOD_TEMPLATE, time));
    }

    @GetMapping("/wait5")
    public Data waitFive(){
        long time = 5;
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            return new Data(UUID.randomUUID(), String.format(BAD_TEMPLATE, time));
        }
        return new Data(UUID.randomUUID(), String.format(GOOD_TEMPLATE, time));
    }

    private record Data(UUID id, String info){}
}
