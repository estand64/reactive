package com.example.reactive.controllers;

import com.example.reactive.services.BlockingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

@RestController
public class BlockingController {
    private static final String RETURN_VAL = "Call took a total of %f seconds!";
    private final BlockingService blockingService;

    public BlockingController(BlockingService blockingService) {
        this.blockingService = blockingService;
    }

    @GetMapping("/firstMultiThread")
    public String first(){
        var time = makeCall(() -> blockingService.firstLogicalThought());
        return String.format(RETURN_VAL, time);
    }

    @GetMapping("/secondMultiThread")
    public String second(){
        var time = makeCall(() -> blockingService.secondPass());
        return String.format(RETURN_VAL, time);
    }

    @GetMapping("/fastMultiThread")
    public String fast(){
        var time = makeCall(() -> blockingService.correct());
        return String.format(RETURN_VAL, time);
    }

    private double makeCall(Supplier<Void> call){
        var before = Instant.now();
        call.get();
        var after = Instant.now();
        var total = Duration.between(before, after);
        return total.getNano()/10000000.0;
    }
}
