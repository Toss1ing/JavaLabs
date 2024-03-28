package com.example.todolist.service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exeption.BadRequestException;
import com.example.todolist.exeption.ObjectExistException;
import com.example.todolist.exeption.ObjectNotFoundException;
import com.example.todolist.model.ToDoItem;
import com.example.todolist.model.ToDoUser;
import com.example.todolist.repository.ToDoItemRepository;
import com.example.todolist.repository.ToDoUserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(ToDoItemService.class);

    public List<ToDoUser> getToDoUser() throws ObjectNotFoundException {
        List <ToDoUser> users = toDoUserRepository.findAll();
        if(users.isEmpty()){
            throw new ObjectNotFoundException("List of users is not founded");
        }
        logger.info("List of users is founded");
        return users;
    }

    public ToDoUser getToDoUserById(Integer id) throws ObjectNotFoundException {
        Integer hash = Objects.hashCode(id);
        Optional<ToDoUser> user;
        if(cacheService.containsKey(hash)){
            logger.info("Searching user in hashMap");
            user = cacheService.get(hash);
            return user.get();
        }
        user = toDoUserRepository.findById(id);
        if(user.isEmpty()){
            throw new ObjectNotFoundException("Object with id " + id + " is not founded");
        }
        logger.info("User with id " + id + " is founded");
        cacheService.put(hash, user);
        return user.get();
    }

    public ToDoUser getToDoUserByName(String userName) throws ObjectNotFoundException {
        Optional<ToDoUser> user =  toDoUserRepository.findByName(userName);
        if(user.isEmpty()){
            throw new ObjectNotFoundException("User with name " + userName + " is not founded");
        }
        logger.info("User with name " + userName + " is founded");
        return user.get();
    }
        
    public void deleteUserById(Integer id) throws BadRequestException{
        Optional<ToDoUser> user = toDoUserRepository.findById(id);
        if(user.isEmpty()){
            throw new BadRequestException("Bad request user with id " + id + " is not founded");
        }
        logger.info("User with id " + id + " is deleted");
        Integer hash = Objects.hashCode(id);
        if(cacheService.containsKey(hash)){
            cacheService.remove(hash);
        }
        toDoUserRepository.deleteById(id);
        return;
    }
    
    public void deleteAllUser() {
        logger.info("All users are deleted");
        cacheService.clear();
        toDoUserRepository.deleteAll();
    }

    public ToDoUser addUser(ToDoUser toDoUser) throws ObjectExistException {
        //need to add hashing process
        Optional<ToDoUser> user = toDoUserRepository.findByName(toDoUser.getLoginName());
        if(user.isPresent()){
            throw new ObjectExistException("User with name " + toDoUser.getLoginName() + " is existed");
        }
        logger.info("User with id " + toDoUser.getId() + " and name" + toDoUser.getLoginName() + " is created");
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
            throw new BadRequestException("Can't find user with name " + userName);
        }        
        Optional<ToDoItem> item = toDoItemRepository.findById(taskId);
        logger.info("task with id " + taskId + " was added to user with name " + userName);
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
            throw new BadRequestException("Can't find user with id " + userId );
        }
        Optional<ToDoItem> item = toDoItemRepository.findById(taskId);
        if(item.isEmpty()){
            throw new BadRequestException("Can't find task with id " + taskId);
        }
        logger.info("Task with id " + taskId + " was deleted in user with id " + userId);
        user.get().getToDoItems().remove(item.get());
        toDoUserRepository.save(user.get());
    }

    public void updateUserNameById(Integer userId, String newName) throws BadRequestException {
        Optional<ToDoUser> user = toDoUserRepository.findById(userId);
        if(user.isEmpty()){
            throw new BadRequestException("User with id " + userId + " is not founded");
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
            throw new ObjectNotFoundException("Users with task id " + taskId + " is not founded");
        }
        return usersByTaskId;
    }
}


