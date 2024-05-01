package com.example.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication

public class ToDoListApplication {

    @GetMapping(path = "/")
    public static void main(final String[] args) {

        SpringApplication.run(ToDoListApplication.class, args);
    }

}
