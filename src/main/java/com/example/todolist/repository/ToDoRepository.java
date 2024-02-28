package com.example.todolist.repository;

import com.example.todolist.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository
        extends JpaRepository<ToDoItem, Integer> {

}
