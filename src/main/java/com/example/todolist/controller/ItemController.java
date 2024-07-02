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
import com.example.todolist.service.CounterService;
import com.example.todolist.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/todo")
@AllArgsConstructor
@Tag(name = "Tasks", description = "Direction with tasks")
public class ItemController {

    private final ItemService itemService;

    private final CounterService counterService;

    @GetMapping(path = "/item")
    @Operation(summary = "Get tasks", description = "Get list of task")
    public ResponseEntity<List<Item>> getToDoItems() throws ObjectNotFoundException {
        counterService.incrementCounter();
        return new ResponseEntity<>(itemService.getToDoItems(), HttpStatus.OK);
    }

    @GetMapping(path = "/item/id/{id}")
    @Operation(summary = "Get task", description = "Get task by id")
    public ResponseEntity<Item> getToDoItemById(@PathVariable final Integer id)
            throws ObjectNotFoundException {
        counterService.incrementCounter();
        return new ResponseEntity<>(itemService.getToDoItemById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/item/search/description/{keyWord}")
    @Operation(summary = "Get tasks", description = "Get tasks by word in description of task")
    public ResponseEntity<List<Item>> getToDoItemByWord(@PathVariable final String keyWord)
            throws ObjectNotFoundException {
        counterService.incrementCounter();
        return new ResponseEntity<>(itemService.getToDoItemByWord(keyWord), HttpStatus.OK);
    }

    @GetMapping(path = "item/name/{taskName}")
    @Operation(summary = "Get task", description = "Get task by name")
    public ResponseEntity<Item> getToDoItemByName(@PathVariable final String taskName) throws ObjectNotFoundException {
        counterService.incrementCounter();
        return new ResponseEntity<>(itemService.getToDoItemByName(taskName), HttpStatus.OK);
    }

    @PostMapping(path = "/item/add")
    @Operation(summary = "Add task")
    public ResponseEntity<Item> addToDoItem(@RequestBody final Item toDoItem) throws ObjectExistException {
        counterService.incrementCounter();
        return new ResponseEntity<>(itemService.save(toDoItem), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/item/delete/id/{id}")
    @Operation(summary = "Delete task", description = "Delete task by id")
    public ResponseEntity<Item> deleteToDoItemById(@PathVariable final Integer id) throws BadRequestException {
        counterService.incrementCounter();
        return new ResponseEntity<>(itemService.deleteToDoItemById(id), HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/item/new/id/{id}/name/{taskName}")
    public ResponseEntity<Item> updateTaskNameById(@PathVariable final Integer id,
            @PathVariable final String taskName) throws ObjectNotFoundException {
        counterService.incrementCounter();
        return new ResponseEntity<>(itemService.updateNameById(id, taskName), HttpStatus.OK);
    }

    @DeleteMapping(path = "item/deleteAll")
    @Operation(summary = "Delete task", description = "Delete all task")
    public ResponseEntity<HttpStatus> deleteAllItems() {
        counterService.incrementCounter();
        itemService.deleteAllItem();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "item/complete/{itemId}")
    @Operation(summary = "Complete task")
    public ResponseEntity<Item> completeTaskById(@PathVariable final Integer itemId) throws BadRequestException {
        counterService.incrementCounter();
        return new ResponseEntity<>(itemService.completeTaskById(itemId), HttpStatus.OK);
    }
}
