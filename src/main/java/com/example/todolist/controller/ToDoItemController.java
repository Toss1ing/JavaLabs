package com.example.todolist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.ToDoItem;
import com.example.todolist.service.ToDoItemService;


import lombok.AllArgsConstructor;


@RestController
@RequestMapping(path = "/api/v1/todo")
@AllArgsConstructor
public class ToDoItemController{

    private final ToDoItemService toDoItemService;


    @GetMapping(path = "/item")
    public ResponseEntity<List<ToDoItem>> getToDoItems() throws ObjectNotFoundException {
        return new ResponseEntity<>(toDoItemService.getToDoItems(), HttpStatus.OK);
    }

    @GetMapping(path = "/item/id/{id}")
    public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable Integer id) throws ObjectNotFoundException {
        return new ResponseEntity<>(toDoItemService.getToDoItemById(id) ,HttpStatus.OK);
    }

    @GetMapping(path = "/item/search/description/{keyWord}")
    public ResponseEntity<List<ToDoItem>> getToDoItemByWord(@PathVariable String keyWord) throws ObjectNotFoundException {
        return new ResponseEntity<>(toDoItemService.getToDoItemByWord(keyWord), HttpStatus.OK);
    }

    @GetMapping(path = "item/name/{taskName}")
    public ResponseEntity<ToDoItem> getToDoItemByName(@PathVariable String taskName) throws ObjectNotFoundException {
        return new ResponseEntity<>(toDoItemService.getToDoItemByName(taskName) ,HttpStatus.OK);
    }

    @PostMapping(path = "/item/add")
    public ResponseEntity<ToDoItem> addToDoItem(@RequestBody ToDoItem toDoItem) throws ObjectExistException {
        return new ResponseEntity<>(toDoItemService.save(toDoItem) ,HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/item/delete/id/{id}")
    public ResponseEntity<HttpStatus> deleteToDoItemById(@PathVariable Integer id) throws BadRequestException {
        toDoItemService.deleteToDoItemById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "item/deleteAll")
    public ResponseEntity<HttpStatus> deleteAllItems() {
        toDoItemService.deleteAllItem();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "item/complete/{itemId}")
    public ResponseEntity<HttpStatus> completeTaskById(@PathVariable  Integer itemId) throws BadRequestException{
        toDoItemService.completeTaskById(itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
