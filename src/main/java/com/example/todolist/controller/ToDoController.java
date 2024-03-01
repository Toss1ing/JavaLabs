package com.example.todolist.controller;

import com.example.todolist.model.ToDoItem;
import com.example.todolist.service.ToDoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/todo")
@AllArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    @GetMapping(path = "/items")
    public List<ToDoItem> getToDoItems(){
        return toDoService.getToDoItems();
    }

    @GetMapping(path = "/item/{id}")
    public ToDoItem getToDoItemById(@PathVariable Integer id){
        return toDoService.getToDoItemById(id);
    }

}
