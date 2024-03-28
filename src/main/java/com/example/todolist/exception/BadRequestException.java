package com.example.todolist.exception;

public class BadRequestException extends Exception {

    public BadRequestException(final String message){
        super(message);
    }
    
}
