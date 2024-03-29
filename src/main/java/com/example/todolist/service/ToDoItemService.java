package com.example.todolist.service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.ToDoItem;
import com.example.todolist.repository.ToDoItemRepository;

import lombok.AllArgsConstructor;

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

    private final Integer allContains = 1111;

    private void updateCacheService(){
        if(cacheService.containsKey(allContains) == false){
            List<ToDoItem> toDoItems = toDoItemRepository.findAll();
            for(ToDoItem item: toDoItems){
                if(cacheService.containsKey(item.getId()) == false){
                    int hash = Objects.hash(item.getId());
                    cacheService.put(hash, Optional.of(item));
                }
            }
        }
        cacheService.put(allContains, null);
    }

    public List<ToDoItem> getToDoItems() throws ObjectNotFoundException {
        updateCacheService();
        List<ToDoItem> items = toDoItemRepository.findAll();
        if(items.isEmpty()){
            throw new ObjectNotFoundException("List of tasks not founded");
        }
        return toDoItemRepository.findAll();
    }

    public ToDoItem getToDoItemById(Integer id) throws ObjectNotFoundException {
        Integer hash = Objects.hashCode(id);
        Optional<ToDoItem> item;
        if(cacheService.containsKey(hash)){
            item = cacheService.get(hash);
        }
        else{
            item = toDoItemRepository.findById(id);
            cacheService.put(hash, item);
        }
        if(item.isEmpty()){
            throw new ObjectNotFoundException(id.toString());
        }
        return item.get();
    }

    public ToDoItem getToDoItemByName(String taskName) throws ObjectNotFoundException {
        Optional<ToDoItem> item = toDoItemRepository.findByName(taskName);
        if(item.isEmpty()){
            throw new ObjectNotFoundException(taskName);
        }
        return item.get();
    }

    public ToDoItem save(ToDoItem toDoItem) throws ObjectExistException {
        Optional<ToDoItem> item = toDoItemRepository.findByName(toDoItem.getNameTask());
        if(item.isPresent()){
            throw new ObjectExistException(toDoItem.getNameTask());
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
            throw new BadRequestException(id.toString());
        }
        Integer hash = Objects.hashCode(id);
        if(cacheService.containsKey(hash)){
            cacheService.remove(hash);
        }
        toDoItemRepository.deleteById(id);
    }

    public void deleteAllItem() {
        cacheService.clear();
        toDoItemRepository.deleteAll();
    }

    public void completeTaskById(Integer taskId) throws BadRequestException {
        Optional<ToDoItem> item;
        Integer hash = Objects.hash(taskId);
        if(cacheService.containsKey(hash)){
            item = cacheService.get(hash);
        }
        else{
            item = toDoItemRepository.findById(taskId);
        }
        if(item.isEmpty()){
            throw new BadRequestException(taskId.toString());
        }
        item.get().setComplete(true);
        item.get().setCompletionDate(Instant.now());
        if(cacheService.containsKey(hash)){
            cacheService.remove(hash);
        }
        cacheService.put(hash, item);
        toDoItemRepository.save(item.get());
    }

    public List<ToDoItem> getToDoItemByWord(String keyWord) throws ObjectNotFoundException {
        List<ToDoItem> listItemByWord = toDoItemRepository.findByDescriptionTask(keyWord);
        if(listItemByWord.isEmpty()){
            throw new ObjectNotFoundException(keyWord);
        }
        return listItemByWord;
    }
}


