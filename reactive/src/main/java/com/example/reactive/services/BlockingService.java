package com.example.reactive.services;

import com.example.reactive.dto.ReturnedItem;
import com.example.reactive.webclients.BlockingWebClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class BlockingService {
    private static final String CALL_MADE_TEXT = "Call %s made.";
    private static final String CALL_RETURNED_TEXT = "Call %s returned.";
    private final ExecutorService executorService;
    private final BlockingWebClient webClient;

    public BlockingService(BlockingWebClient webClient) {
        this.webClient = webClient;
        executorService = Executors.newFixedThreadPool(6);
    }

    public Void firstLogicalThought(){
        CallData callA = new CallData("A1", webClient::callFive);
        CallData callA2 = new CallData("A2", webClient::callTwo);
        var prereqCall = processCall(callA,null, callA);
        var returnedCall = processCall(callA, prereqCall, callA2);
        try {
            returnedCall.get();
            System.out.println(String.format(CALL_RETURNED_TEXT, callA2.name));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callA2.name());
            e.printStackTrace();
        }

        CallData callB = new CallData("B1", webClient::callFour);
        CallData callB2 = new CallData("B2", webClient::callOne);
        CallData callB3 = new CallData("B3", webClient::callOne);
        prereqCall = processCall(callB,null, callB);
        returnedCall = processCall(callB, prereqCall, callB2);
        try {
            returnedCall.get();
            System.out.println(String.format(CALL_RETURNED_TEXT, callB2.name));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB2.name());
            e.printStackTrace();
        }
        returnedCall = processCall(callB, prereqCall, callB3);
        try {
            returnedCall.get();
            System.out.println(String.format(CALL_RETURNED_TEXT, callB3.name));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB2.name());
            e.printStackTrace();
        }
        return null;
    }

    public Void secondPass(){
        CallData callA = new CallData("Call to A1", webClient::callFive);
        CallData callB = new CallData("Call to B1", webClient::callFour);
        CallData callA2 = new CallData("Call to A2", webClient::callTwo);
        CallData callB2 = new CallData("Call to B2", webClient::callOne);
        CallData callB3 = new CallData("Call to B3", webClient::callOne);

        var prereqCallA = processCall(callA, null, callA);
        var prereqCallB = processCall(callB, null, callB);

        try {
            var returnedCallA = processCall(callA, prereqCallA, callA2);
            returnedCallA.get();
            System.out.println(String.format(CALL_RETURNED_TEXT, callA2.name));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callA2.name());
            e.printStackTrace();
        }

        Future<ReturnedItem> returnedCallB;
        try {
            returnedCallB = processCall(callB, prereqCallB, callB2);
            returnedCallB.get();
            System.out.println(String.format(CALL_RETURNED_TEXT, callB2.name));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB2.name());
            e.printStackTrace();
        }

        try {
            returnedCallB = processCall(callB, prereqCallB, callB3);
            returnedCallB.get();
            System.out.println(String.format(CALL_RETURNED_TEXT, callB3.name));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB3.name());
            e.printStackTrace();
        }
        return null;
    }

    public Void correct(){
        CallData callA = new CallData("Call to A1", webClient::callFive);
        CallData callB = new CallData("Call to B1", webClient::callFour);
        CallData callA2 = new CallData("Call to A2", webClient::callTwo);
        CallData callB2 = new CallData("Call to B2", webClient::callOne);
        CallData callB3 = new CallData("Call to B3", webClient::callOne);

        var prereqCallA = processCall(callA, null, callA);
        var prereqCallB = processCall(callB, null, callB);


        Future<Future<ReturnedItem>> returnedCallA = executorService.submit(() -> processCall(callA, prereqCallA, callA2));
        try {
            returnedCallA.get().get();
            System.out.println(String.format(CALL_RETURNED_TEXT, callA2.name));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callA2.name());
            e.printStackTrace();
        }

        Future<Future<ReturnedItem>> returnedCallB2 = executorService.submit(() -> processCall(callB, prereqCallB, callB2));
        try {
            returnedCallB2.get().get();
            System.out.println(String.format(CALL_RETURNED_TEXT, callB2.name));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB2.name());
            e.printStackTrace();
        }

        Future<Future<ReturnedItem>> returnedCallB3 = executorService.submit(() -> processCall(callB, prereqCallB, callB3));
        try {
            returnedCallB3.get().get();
            System.out.println(String.format(CALL_RETURNED_TEXT, callB3.name));
        } catch (InterruptedException  | ExecutionException e) {
            System.out.println("something went wrong with the call to " + callB3.name());
            e.printStackTrace();
        }
        return null;
    }

    private Future<ReturnedItem> processCall(CallData prereqCall, Future<ReturnedItem> prereq, CallData call){
        if(prereq != null){
            try {
                var returnedValue = prereq.get();
                System.out.println(String.format(CALL_RETURNED_TEXT, prereqCall.name()));
                System.out.println(returnedValue.id());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(String.format(CALL_MADE_TEXT, call.name()));
        return executorService.submit(call.call());
    }

    private static record CallData(String name, Callable<ReturnedItem> call){}
}
