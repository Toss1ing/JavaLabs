package com.example.todolist.service;

import com.example.todolist.model.ToDoItem;
import com.example.todolist.repository.ToDoRepository;
import lombok.AllArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ToDoService {

    ToDoRepository toDoRepository;

    public List<ToDoItem> getToDoItems() {
        return toDoRepository.findAll();
    }

    public ToDoItem getToDoItemById(Integer id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new ExpressionException("Not found item by id"));
    }
}
