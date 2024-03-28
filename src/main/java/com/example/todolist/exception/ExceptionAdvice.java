package com.example.todolist.exeption;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice()
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Response> NotFoundException(ObjectNotFoundException ex){
        logger.info("Exception: " + ex.getMessage());
        Response response = new Response(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> RequestException(BadRequestException ex){
        logger.info("Exception: " + ex.getMessage());
        Response response = new Response(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectExistException.class)
    public ResponseEntity<Response> ExistException(ObjectExistException ex) {
        logger.info("Exeption: " + ex.getMessage());
        Response response = new Response(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }

}
