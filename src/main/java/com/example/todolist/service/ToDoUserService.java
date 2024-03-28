package com.example.todolist.service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.ToDoItem;
import com.example.todolist.model.ToDoUser;
import com.example.todolist.repository.ToDoItemRepository;
import com.example.todolist.repository.ToDoUserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ToDoUserService {

    ToDoUserRepository toDoUserRepository;

    ToDoItemRepository toDoItemRepository;

    CacheService<Integer,Optional<ToDoUser>> cacheService;

    public List<ToDoUser> getToDoUser() throws ObjectNotFoundException {
        List <ToDoUser> users = toDoUserRepository.findAll();
        if(users.isEmpty()){
            throw new ObjectNotFoundException("List of users is not founded");
        }
        return users;
    }

    public ToDoUser getToDoUserById(Integer id) throws ObjectNotFoundException {
        Integer hash = Objects.hashCode(id);
        Optional<ToDoUser> user;
        if(cacheService.containsKey(hash)){
            user = cacheService.get(hash);
            if(user.isPresent()){
                return user.get();
            }
        }
        user = toDoUserRepository.findById(id);
        if(user.isEmpty()){
            throw new ObjectNotFoundException(id.toString());
        }
        cacheService.put(hash, user);
        return user.get();
    }

    public ToDoUser getToDoUserByName(String userName) throws ObjectNotFoundException {
        Optional<ToDoUser> user =  toDoUserRepository.findByName(userName);
        if(user.isEmpty()){
            throw new ObjectNotFoundException(userName);
        }
        return user.get();
    }
        
    public void deleteUserById(Integer id) throws BadRequestException{
        Optional<ToDoUser> user = toDoUserRepository.findById(id);
        if(user.isEmpty()){
            throw new BadRequestException(id.toString());
        }
        Integer hash = Objects.hashCode(id);
        if(cacheService.containsKey(hash)){
            cacheService.remove(hash);
        }
        toDoUserRepository.deleteById(id);
    }
    
    public void deleteAllUser() {
        cacheService.clear();
        toDoUserRepository.deleteAll();
    }

    public ToDoUser addUser(ToDoUser toDoUser) throws ObjectExistException {
        Optional<ToDoUser> user = toDoUserRepository.findByName(toDoUser.getLoginName());
        if(user.isPresent()){
            throw new ObjectExistException(toDoUser.getLoginName());
        }
        if(toDoUser.getToDoItems() == null){
            toDoUser.setToDoItems(new HashSet<>());
        }
        Integer hash = Objects.hash(toDoUser.getId());
        cacheService.put(hash,user);
        return toDoUserRepository.save(toDoUser);
    }

    @Transactional
    public ToDoUser addTaskInUserById(String userName, Integer taskId) throws BadRequestException {
        Optional<ToDoUser> user = toDoUserRepository.findByName(userName);
        if(user.isEmpty()){
            throw new BadRequestException(userName);
        }        
        Optional<ToDoItem> item = toDoItemRepository.findById(taskId);
        user.get().getToDoItems().add(item.get());
        Integer hash = Objects.hash(user.get().getId());
        if(cacheService.containsKey(hash)){
            cacheService.remove(hash);
        }
        cacheService.put(hash, user);
        return toDoUserRepository.save(user.get());
    }

    @Transactional
    public void deleteTaskByIdInUser(Integer userId, Integer taskId) throws BadRequestException {
        Optional<ToDoUser> user = toDoUserRepository.findById(userId);
        if(user.isEmpty()){
            throw new BadRequestException(userId.toString());
        }
        Optional<ToDoItem> item = toDoItemRepository.findById(taskId);
        if(item.isEmpty()){
            throw new BadRequestException(taskId.toString());
        }
        user.get().getToDoItems().remove(item.get());
        toDoUserRepository.save(user.get());
    }

    public void updateUserNameById(Integer userId, String newName) throws BadRequestException {
        Optional<ToDoUser> user = toDoUserRepository.findById(userId);
        if(user.isEmpty()){
            throw new BadRequestException(userId.toString());
        }
        user.get().setLoginName(newName);
        if(cacheService.containsKey(userId)){
            cacheService.remove(userId);
        }
        cacheService.put(userId, user);
        toDoUserRepository.save(user.get());
    }

    public List<ToDoUser> getUsersWithTaskById(Integer taskId) throws ObjectNotFoundException {
        List<ToDoUser> usersByTaskId = toDoUserRepository.findUserWithTaskById(taskId);
        if(usersByTaskId.isEmpty()){
            throw new ObjectNotFoundException(taskId.toString());
        }
        return usersByTaskId;
    }
}


