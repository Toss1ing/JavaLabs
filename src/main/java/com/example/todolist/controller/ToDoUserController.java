package com.example.todolist.controller;

import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.ToDoUser;
import com.example.todolist.service.ToDoUserService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/todo")
@AllArgsConstructor
public class ToDoUserController {

    private final ToDoUserService toDoUserService;

    @GetMapping(path = "/user")
    public ResponseEntity<List<ToDoUser>> getToDoUser() throws ObjectNotFoundException { 
        List<ToDoUser> users = toDoUserService.getToDoUser();
        if(users.isEmpty()){
            throw new ObjectNotFoundException("Can't find all users");
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/user/id/{id}")
    public  ResponseEntity<ToDoUser> getToDoUserById(@PathVariable Integer id) throws ObjectNotFoundException{
        return new ResponseEntity<>(toDoUserService.getToDoUserById(id), HttpStatus.OK);
    }

    @GetMapping(path = "user/name/{userName}")
    public ResponseEntity<ToDoUser> getToDoUserByName(@PathVariable String userName) throws ObjectNotFoundException{
        return new ResponseEntity<>( toDoUserService.getToDoUserByName(userName), HttpStatus.OK);
    }

    @GetMapping(path = "user/search/task/id/{taskId}")
    public ResponseEntity<List<ToDoUser>> getToDoUsersWithTaskById(@PathVariable Integer taskId) throws ObjectNotFoundException {
        return new ResponseEntity<>(toDoUserService.getUsersWithTaskById(taskId), HttpStatus.OK);
    } 

    @PostMapping(path = "/user/add")
    public ResponseEntity<ToDoUser> addUser(@Validated @RequestBody ToDoUser toDoUser) throws ObjectExistException{
        return new ResponseEntity<>(toDoUserService.addUser(toDoUser),HttpStatus.CREATED);
    }

    @PostMapping(path = "/user/add/{userName}/{taskId}")
    public ResponseEntity<ToDoUser> addTaskByIdInUser(@PathVariable String userName, @PathVariable Integer taskId) throws BadRequestException{
        return new ResponseEntity<>(toDoUserService.addTaskInUserById(userName, taskId), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/user/delete/id/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Integer id) throws BadRequestException {
        toDoUserService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/user/deleteAll")
    public ResponseEntity<HttpStatus> deleteAllUser() {
        toDoUserService.deleteAllUser();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/user/delete/{userId}/{taskId}")
    public ResponseEntity<HttpStatus> deleteTaskInUserById(@PathVariable Integer userId, @PathVariable Integer taskId) throws BadRequestException {
        toDoUserService.deleteTaskByIdInUser(userId, taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/user/name/new/{userId}")
    public ResponseEntity<HttpStatus> updateUserNameById(@PathVariable Integer userId, @RequestBody String newName) throws BadRequestException {
        toDoUserService.updateUserNameById(userId, newName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
