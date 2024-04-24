package com.example.todolist.controller;

import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.User;
import com.example.todolist.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/todo")
@AllArgsConstructor
@Tag(name = "Users", description = "Direction with users")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/user")
    @Operation(summary = "Get users", description = "Get list of users")
    public ResponseEntity<List<User>> getToDoUser() throws ObjectNotFoundException { 
        List<User> users = userService.getToDoUser();
        if(users.isEmpty()){
            throw new ObjectNotFoundException("Can't find all users");
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/user/id/{id}")
    @Operation(summary = "Get user", description = "Get user by id")
    public  ResponseEntity<User> getToDoUserById(@PathVariable Integer id) throws ObjectNotFoundException {
        return new ResponseEntity<>(userService.getToDoUserById(id), HttpStatus.OK);
    }

    @GetMapping(path = "user/name/{userName}")
    @Operation(summary = "Get user", description = "Get user by name")
    public ResponseEntity<User> getToDoUserByName(@PathVariable String userName) throws ObjectNotFoundException {
        return new ResponseEntity<>(userService.getToDoUserByName(userName), HttpStatus.OK);
    }

    @GetMapping(path = "user/search/task/id/{taskId}")
    @Operation(summary = "Get user", description = "Get user by task id")
    public ResponseEntity<List<User>> getToDoUsersWithTaskById(@PathVariable Integer taskId) throws ObjectNotFoundException {
        return new ResponseEntity<>(userService.getUsersWithTaskById(taskId), HttpStatus.OK);
    } 

    @PostMapping(path = "/user/add")
    @Operation(summary = "Add user")
    public ResponseEntity<User> addUser(@Validated @RequestBody User toDoUser) throws ObjectExistException {
        return new ResponseEntity<>(userService.addUser(toDoUser),HttpStatus.CREATED);
    }

    @PostMapping(path = "/user/add/{userName}/{taskId}")
    @Operation(summary = "Add task to user")
    public ResponseEntity<User> addTaskByIdInUser(@PathVariable String userName, @PathVariable Integer taskId) throws BadRequestException {
        return new ResponseEntity<>(userService.addTaskInUserById(userName, taskId), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/user/delete/id/{id}")
    @Operation(summary = "Delete user", description = "Delete user by id")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Integer id) throws BadRequestException {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/user/deleteAll")
    @Operation(summary = "Delete user", description = "Delete all user")
    public ResponseEntity<HttpStatus> deleteAllUser() {
        userService.deleteAllUser();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/user/delete/task/{userId}/{taskId}")
    @Operation(summary = "Delete task", description = "Delete task in user by task id")
    public ResponseEntity<HttpStatus> deleteTaskInUserById(@PathVariable Integer userId, @PathVariable Integer taskId) throws BadRequestException {
        userService.deleteTaskByIdInUser(userId, taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/user/delete/group/{userId}")
    @Operation(summary = "Delete group", description = "Delete group in user by id")
    public ResponseEntity<HttpStatus> deleteGroupInUserById(@PathVariable Integer userId) throws BadRequestException {
        userService.deleteGroupInUserByID(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/user/name/new/{userId}")
    @Operation(summary = "Add user")
    public ResponseEntity<HttpStatus> updateUserNameById(@PathVariable Integer userId, @RequestBody String newName) throws BadRequestException {
        userService.updateUserNameById(userId, newName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/user/add/users")
    @Operation(summary = "Add users", description = "Add list of users")
    public ResponseEntity<List<User>> addListOfUser(@RequestBody List<User> users) throws ObjectExistException {
        return new ResponseEntity<>(userService.addListOfUser(users), HttpStatus.CREATED);
    }

}
