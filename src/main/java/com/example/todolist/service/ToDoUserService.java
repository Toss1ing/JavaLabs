package com.example.todolist.service;


import com.example.todolist.model.ToDoUser;
import com.example.todolist.repository.ToDoUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ToDoUserService {

    ToDoUserRepository toDoUserRepository;

    public List<ToDoUser> getToDoUser(){
        return toDoUserRepository.findAll();
    }

    public ToDoUser getToDoUserById(Integer id) {
        return toDoUserRepository.findById(id)
                .orElseThrow(() -> new ExpressionException("Cannot find user by id"));
    }

    public ToDoUser getToDOUserByName(String userName) {
        return toDoUserRepository.findByName(userName)
                .orElseThrow(() -> new ExpressionException("adsf"));
    }

    public ToDoUser save(ToDoUser toDoUser) {
        return toDoUserRepository.save(toDoUser);
    }

    public void deleteUserById(Integer id) {
        toDoUserRepository.deleteById(id);
    }
    public void deleteAllUser() {
        toDoUserRepository.deleteAll();
    }

}
