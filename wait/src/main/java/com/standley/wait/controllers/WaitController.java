package com.standley.wait.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
public class WaitController {
    private static final String BAD_TEMPLATE = "Didn't sleep %n second cause something went wrong";
    private static final String GOOD_TEMPLATE = "slept %d second";
    private final Random random;

    public WaitController() {
        random = new Random();
    }

    @GetMapping("/wait1")
    public Data waitOne(){
        try {
            System.out.println("sleeping for 1 second");
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
            System.out.println("sleeping for 2 seconds");
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
            System.out.println("sleeping for 3 seconds");
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
            System.out.println("sleeping for 4 seconds");
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
            System.out.println("sleeping for 5 seconds");
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            return new Data(UUID.randomUUID(), String.format(BAD_TEMPLATE, time));
        }
        return new Data(UUID.randomUUID(), String.format(GOOD_TEMPLATE, time));
    }

    @GetMapping("/400error")
    public ResponseEntity<ExtendedData> fourHundredError(){
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/500error")
    public ResponseEntity<ExtendedData> fiveHundredError(){
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/randomError")
    public ResponseEntity<Data> randomError(){
        var flip = random.nextInt(2);
        if(flip == 1){
            System.out.println("returning 503");
            return ResponseEntity.status(503).build();
        }

        System.out.println("returning 200");
        return ResponseEntity.status(200).body(new Data(UUID.randomUUID(), "info"));
    }

    private record Data(UUID id, String info){}
    private record ExtendedData(UUID id, UUID callingId, String info){}
}
