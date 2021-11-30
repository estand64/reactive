package com.example.reactive.services;

import com.example.reactive.dto.ReturnedItem;
import com.example.reactive.webclients.BlockingWebClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class BlockingService {
    private final ExecutorService executorService;
    private final BlockingWebClient webClient;

    public BlockingService(BlockingWebClient webClient) {
        this.webClient = webClient;
        executorService = Executors.newFixedThreadPool(6);
    }

    public void firstLogicalThought(){
        CallData callA = new CallData("Call to A1", webClient::callFive);
        CallData callA2 = new CallData("Call to A2", webClient::callTwo);
        var prereqCall = processCall(null, callA);
        var returnedCall = processCall(prereqCall, callA2);
        System.out.println("Value returned from " + callA2.name + ": " + returnedCall.get());


        CallData callB = new CallData("Call to B1", webClient::callFour);
        CallData callB2 = new CallData("Call to B2", webClient::callOne);
        CallData callB3 = new CallData("Call to B3", webClient::callOne);
        prereqCall = processCall(null, callA);
        returnedCall = processCall(prereqCall, callA2);
        returnedCall = processCall(prereqCall, callA);
    }

    private Future<ReturnedItem> processCall(Future<ReturnedItem> prereq, CallData call){
        if(prereq != null){
            System.out.println("Waiting for a prereq call before processing: " + call.name());
            try {
                var returnedValue = prereq.get();
                System.out.println("Got the value: " + returnedValue.id() + " from the prereq call!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Making a call to " + call.name());
        return executorService.submit(call.call());
    }

    private static record CallData(String name, Callable<ReturnedItem> call){}
}
