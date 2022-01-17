package com.example.reactive;

import org.springframework.http.HttpStatus;

public class MyHttpException extends Exception{
    private final HttpStatus status;

    public MyHttpException(String error, HttpStatus status) {
        super(error);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
