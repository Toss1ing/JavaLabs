package com.example.todolist.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice()
public class ExceptionAdvice {


    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Response> objectNotFoundException(ObjectNotFoundException ex){
        Response response = new Response(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> badRequestException(BadRequestException ex){
        Response response = new Response(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectExistException.class)
    public ResponseEntity<Response> objectExistException(ObjectExistException ex) {
        Response response = new Response(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }

}
