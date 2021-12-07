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

    public void firstLogicalThought() throws ExecutionException, InterruptedException {
        CallData callA = new CallData("Call to A1", webClient::callFive);
        CallData callA2 = new CallData("Call to A2", webClient::callTwo);
        var prereqCall = processCall(null, callA);
        var returnedCall = processCall(prereqCall, callA2);
        System.out.println("Value returned from " + callA2.name + ": " + returnedCall.get());


        CallData callB = new CallData("Call to B1", webClient::callFour);
        CallData callB2 = new CallData("Call to B2", webClient::callOne);
        CallData callB3 = new CallData("Call to B3", webClient::callOne);
        prereqCall = processCall(null, callB);
        returnedCall = processCall(prereqCall, callB2);
        System.out.println("Value returned from " + callB2.name + ": " + returnedCall.get());
        returnedCall = processCall(prereqCall, callB3);
        System.out.println("Value returned from " + callB3.name + ": " + returnedCall.get());
    }

    public void secondPass(){
        CallData callA = new CallData("Call to A1", webClient::callFive);
        CallData callB = new CallData("Call to B1", webClient::callFour);
        CallData callA2 = new CallData("Call to A2", webClient::callTwo);
        CallData callB2 = new CallData("Call to B2", webClient::callOne);
        CallData callB3 = new CallData("Call to B3", webClient::callOne);

        var prereqCallA = processCall(null, callA);
        var prereqCallB = processCall(null, callB);


        try {
            var returnedCallA = processCall(prereqCallA, callA2);
            System.out.println("Value returned from " + callA2.name + ": " + returnedCallA.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callA2.name());
            e.printStackTrace();
        }

        Future<ReturnedItem> returnedCallB;
        try {
            returnedCallB = processCall(prereqCallB, callB2);
            System.out.println("Value returned from " + callB2.name + ": " + returnedCallB.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB2.name());
            e.printStackTrace();
        }

        try {
            returnedCallB = processCall(prereqCallB, callB3);
            System.out.println("Value returned from " + callB3.name + ": " + returnedCallB.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB3.name());
            e.printStackTrace();
        }
    }

    public void correct(){
        CallData callA = new CallData("Call to A1", webClient::callFive);
        CallData callB = new CallData("Call to B1", webClient::callFour);
        CallData callA2 = new CallData("Call to A2", webClient::callTwo);
        CallData callB2 = new CallData("Call to B2", webClient::callOne);
        CallData callB3 = new CallData("Call to B3", webClient::callOne);

        var prereqCallA = processCall(null, callA);
        var prereqCallB = processCall(null, callB);


        Future<Future<ReturnedItem>> returnedCallA = executorService.submit(() -> processCall(prereqCallA, callA2));
        try {
            System.out.println("Value returned from " + callA2.name + ": " + returnedCallA.get().get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callA2.name());
            e.printStackTrace();
        }

        Future<Future<ReturnedItem>> returnedCallB = executorService.submit(() -> processCall(prereqCallB, callB2));
        try {
            System.out.println("Value returned from " + callB2.name + ": " + returnedCallB.get().get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB2.name());
            e.printStackTrace();
        }

        Future<Future<ReturnedItem>> returnedCallB2 = executorService.submit(() -> processCall(prereqCallB, callB3));
        try {
            System.out.println("Value returned from " + callB3.name + ": " + returnedCallB2.get().get());
        } catch (InterruptedException  | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB3.name());
            e.printStackTrace();
        }
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
