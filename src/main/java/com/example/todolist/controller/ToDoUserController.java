package com.example.todolist.controller;

import com.example.todolist.model.ToDoUser;
import com.example.todolist.service.ToDoUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/todo")
@AllArgsConstructor
public class ToDoUserController {

    private final ToDoUserService toDoUserService;

    @GetMapping(path = "/user")
    public List<ToDoUser> getToDoUser(){
        return  toDoUserService.getToDoUser();
    }

    @GetMapping(path = "/user/id/{id}")
    public ToDoUser getToDoUserById(@PathVariable Integer id){
        return toDoUserService.getToDoUserById(id);
    }


    @GetMapping(path = "user/name/{userName}")
    public ToDoUser getToDoUserByName(@PathVariable String userName){
        return toDoUserService.getToDOUserByName(userName);
    }

    @PostMapping(path = "/user/post")
    public ToDoUser addUserWithToDoItem(@RequestBody ToDoUser toDoUser){
        return toDoUserService.save(toDoUser);
    }

    @DeleteMapping(path = "/user/{id}")
    public void deleteUserById(@PathVariable Integer id){
        toDoUserService.deleteUserById(id);
    }

    @DeleteMapping(path = "/user/deleteAll")
    public void deleteAllUser(){
        toDoUserService.deleteAllUser();
    }

}
