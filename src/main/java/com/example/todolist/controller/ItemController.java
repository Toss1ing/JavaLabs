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
import com.example.todolist.model.Item;
import com.example.todolist.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping(path = "/api/v1/todo")
@AllArgsConstructor
@Tag(name = "Tasks", description = "Direction with tasks")
public class ItemController{

    private final ItemService itemService;
    
    @GetMapping(path = "/item")
    @Operation(summary = "Get tasks", description = "Get list of task")
    public ResponseEntity<List<Item>> getToDoItems() throws ObjectNotFoundException {
        return new ResponseEntity<>(itemService.getToDoItems(), HttpStatus.OK);
    }

    @GetMapping(path = "/item/id/{id}")
    @Operation(summary = "Get task", description = "Get task by id")
    public ResponseEntity<Item> getToDoItemById(@PathVariable Integer id) throws ObjectNotFoundException {
        return new ResponseEntity<>(itemService.getToDoItemById(id) ,HttpStatus.OK);
    }

    @GetMapping(path = "/item/search/description/{keyWord}")
    @Operation(summary = "Get tasks", description = "Get tasks by word in description of task")
    public ResponseEntity<List<Item>> getToDoItemByWord(@PathVariable String keyWord) throws ObjectNotFoundException {
        return new ResponseEntity<>(itemService.getToDoItemByWord(keyWord), HttpStatus.OK);
    }

    @GetMapping(path = "item/name/{taskName}")
    @Operation(summary = "Get task", description = "Get task by name")
    public ResponseEntity<Item> getToDoItemByName(@PathVariable String taskName) throws ObjectNotFoundException {
        return new ResponseEntity<>(itemService.getToDoItemByName(taskName) ,HttpStatus.OK);
    }

    @PostMapping(path = "/item/add")
    @Operation(summary = "Add task")
    public ResponseEntity<Item> addToDoItem(@RequestBody Item toDoItem) throws ObjectExistException {
        return new ResponseEntity<>(itemService.save(toDoItem) ,HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/item/delete/id/{id}")
    @Operation(summary = "Delete task", description = "Delete task by id")
    public ResponseEntity<HttpStatus> deleteToDoItemById(@PathVariable Integer id) throws BadRequestException {
        itemService.deleteToDoItemById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "item/deleteAll")
    @Operation(summary = "Delete task", description = "Delete all task")
    public ResponseEntity<HttpStatus> deleteAllItems() {
        itemService.deleteAllItem();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "item/complete/{itemId}")
    @Operation(summary = "Complete task")
    public ResponseEntity<HttpStatus> completeTaskById(@PathVariable  Integer itemId) throws BadRequestException {
        itemService.completeTaskById(itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}