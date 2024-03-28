package com.example.todolist.service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.ToDoItem;
import com.example.todolist.repository.ToDoItemRepository;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.time.Instant;


@Service
@AllArgsConstructor
public class ToDoItemService {

    ToDoItemRepository toDoItemRepository;

    CacheService<Integer,Optional<ToDoItem>> cacheService;

    private final Logger logger = LoggerFactory.getLogger(ToDoItemService.class);

    public List<ToDoItem> getToDoItems() throws ObjectNotFoundException {
        List<ToDoItem> items = toDoItemRepository.findAll();
        if(items.isEmpty()){
            throw new ObjectNotFoundException("List of tasks not founded");
        }
        logger.info("List of tasks is founded");
        return toDoItemRepository.findAll();
    }

    public ToDoItem getToDoItemById(Integer id) throws ObjectNotFoundException {
        Integer hash = Objects.hashCode(id);
        Optional<ToDoItem> item;
        if(cacheService.containsKey(hash)){
            logger.info("Found task with id " + id + " in hashMap");
            item = cacheService.get(hash);
            if(item.isPresent()){
                return item.get();
            }
        }
        item = toDoItemRepository.findById(id);
        if(item.isEmpty()){
            throw new ObjectNotFoundException("Task with id " + id + " is not founded");
        }
        logger.info("Task with id " + id + " is founded");
        return item.get();
    }

    public ToDoItem getToDoItemByName(String taskName) throws ObjectNotFoundException {
        Optional<ToDoItem> item = toDoItemRepository.findByName(taskName);
        if(item.isEmpty()){
            throw new ObjectNotFoundException("Object with task name " + taskName + " not founded");
        }
        logger.info("Object with task name" + taskName + " is founded");
        return item.get();
    }

    public ToDoItem save(ToDoItem toDoItem) throws ObjectExistException {
        //add hashing process
        Optional<ToDoItem> item = toDoItemRepository.findByName(toDoItem.getNameTask());
        if(item.isPresent()){
            throw new ObjectExistException("Tasks with name " + toDoItem.getNameTask() + " is existed");
        } 
        if(toDoItem.getUsers() == null){
            toDoItem.setUsers(new ArrayList<>());
        }
        Integer hash = Objects.hash(toDoItem.getId());
        cacheService.put(hash, item);
        return toDoItemRepository.save(toDoItem);
    }

    public void deleteToDoItemById(Integer id) throws BadRequestException {
        if(toDoItemRepository.findById(id).isEmpty()){
            throw new BadRequestException("Can't find item with id " + id + " is not founded");
        }
        logger.info("Item with id " + id + " is deleted");
        Integer hash = Objects.hashCode(id);
        cacheService.remove(hash);
        toDoItemRepository.deleteById(id);
    }

    public void deleteAllItem() {
        logger.info("All items being deleted");
        cacheService.clear();
        toDoItemRepository.deleteAll();
    }

    public void completeTaskById(Integer taskId) throws BadRequestException {
        Optional<ToDoItem> item = toDoItemRepository.findById(taskId);
        if(item.isEmpty()){
            throw new BadRequestException("Task with id " + taskId + "is not founded");
        }
        item.get().setComplete(true);
        item.get().setCompletionDate(Instant.now());
        toDoItemRepository.save(item.get());
    }

    public List<ToDoItem> getToDoItemByWord(String keyWord) throws ObjectNotFoundException {
        List<ToDoItem> listItemByWord = toDoItemRepository.findByDescriptionTask(keyWord);
        if(listItemByWord.isEmpty()){
            throw new ObjectNotFoundException("Tasks with string in description" + keyWord + " is not founded");
        }
        return listItemByWord;
    }
}


