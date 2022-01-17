package com.example.reactive.controllers;

import com.example.reactive.dto.ReturnObject;
import com.example.reactive.services.CompareService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Supplier;

@RestController
public class ComparisionController {
    private static final String DIVIDER = "--------------------------------------------------------";
    private final CompareService sameCall;

    public ComparisionController(CompareService sameCall) {
        this.sameCall = sameCall;
    }

    @GetMapping("/basicCallSame")
    public ReturnObject basicCallSame(){
        var returnVal = makeCall(sameCall::twoSameCallsBasic);
        return new ReturnObject(returnVal, null);
    }

    @GetMapping("/threadCallSame")
    public ReturnObject threadCallSame(){
        var returnVal = makeCall(sameCall::twoSameCallsThread);
        return new ReturnObject(returnVal, null);
    }

    @GetMapping("/reactiveCallSame")
    public Mono<ReturnObject> reactiveCallSame(){
        return sameCall.twoSameCallsReactive(Instant.now());
    }

    private <T> T makeCall(Supplier<T> call){
        System.out.println(DIVIDER);
        var before = Instant.now();
        var returnVal = call.get();
        var after = Instant.now();
        System.out.println("Call initiated at " + before);
        System.out.println("Call end at " + after);
        var millis = after.toEpochMilli() - before.toEpochMilli();
        System.out.println("Time in seconds to make call: " + millis/1000.0);
        System.out.println(DIVIDER);
        return returnVal;
    }
}
