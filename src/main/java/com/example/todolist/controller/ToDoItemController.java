package com.example.todolist.controller;

import com.example.todolist.model.ToDoItem;
import com.example.todolist.service.ToDoItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/todo")
@AllArgsConstructor
public class ToDoItemController {

    private final ToDoItemService toDoItemService;

    @GetMapping(path = "/items")
    public List<ToDoItem> getToDoItems(){
        return toDoItemService.getToDoItems();
    }

    @GetMapping(path = "/item/id/{id}")
    public ToDoItem getToDoItemById(@PathVariable Integer id){
        return toDoItemService.getToDoItemById(id);
    }

    @GetMapping(path = "item/name/{taskName}")
    public ToDoItem getToDoItemByName(@PathVariable String taskName){
        return toDoItemService.getToDoItemByName(taskName);
    }

    @PostMapping(path = "/item/post")
    public ToDoItem addToDoItem(@RequestBody ToDoItem toDoItem){
        return  toDoItemService.save(toDoItem);
    }

    @DeleteMapping(path = "/item/delete/{id}")
    public void deleteToDoItemById(@PathVariable Integer id){
        toDoItemService.deleteToDoItemById(id);
    }
    @DeleteMapping(path = "item/deleteAll")
    public void deleteAllItems(){
        toDoItemService.deleteAllItem();
    }


}
