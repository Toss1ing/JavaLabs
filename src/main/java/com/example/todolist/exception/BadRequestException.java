package com.example.todolist.exeption;

public class BadRequestException extends Exception {

    public BadRequestException(final String message){
        super(message);
    }
    
}
