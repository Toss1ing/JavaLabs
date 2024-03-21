package com.example.todolist.service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.model.ToDoItem;
import com.example.todolist.repository.ToDoItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ToDoItemService {

    ToDoItemRepository toDoItemRepository;

    CacheService<ToDoItem,String> cacheService;


    public List<ToDoItem> getToDoItems() {
        return toDoItemRepository.findAll();
    }

    public ToDoItem getToDoItemById(Integer id) {
        return toDoItemRepository.findById(id)
                .orElseThrow(() -> new ExpressionException("Not found item by id"));
    }

    public ToDoItem getToDoItemByName(String taskName) {
        return toDoItemRepository.findByName(taskName)
                .orElseThrow(() -> new ExpressionException("Cannot find item by taskName"));
    }

    public ToDoItem save(ToDoItem toDoItem) {
        return  toDoItemRepository.save(toDoItem);
    }

    public void deleteToDoItemById(Integer id) {
        toDoItemRepository.deleteById(id);
    }

    public void deleteAllItem() {
        toDoItemRepository.deleteAll();
    }
}
