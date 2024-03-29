package com.example.todolist.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.ToDoUser;
import com.example.todolist.model.ToDoUserGroup;
import com.example.todolist.repository.ToDoUserGroupRepository;
import com.example.todolist.repository.ToDoUserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ToDoUserGroupService {
    
    ToDoUserGroupRepository toDoUserGroupRepository;

    ToDoUserRepository toDoUserRepository;
    
    CacheService<Integer, Optional<ToDoUserGroup>> cacheService;

    private final static Integer allContains = 1111;

    private void updateCacheService(){
        if(cacheService.containsKey(allContains) == false){
            List<ToDoUserGroup> toDoUserGroups = toDoUserGroupRepository.findAll();
            for(ToDoUserGroup group: toDoUserGroups){
                if(cacheService.containsKey(group.getId()) == false){
                    int hash = Objects.hash(group.getId());
                    cacheService.put(hash, Optional.of(group));
                }
            }
        }
        cacheService.put(allContains, null);
    }

    public List<ToDoUserGroup> getAllUserGroup() throws ObjectNotFoundException{
        updateCacheService();
        List<ToDoUserGroup> userGroups = toDoUserGroupRepository.findAll();
        if(userGroups.isEmpty()){
            throw new ObjectNotFoundException("Can't find all group");
        }
        return userGroups;
    }

    public ToDoUserGroup getUserGroupById(Integer id) throws ObjectNotFoundException {
        Optional<ToDoUserGroup> userGroup;
        Integer hash = Objects.hash(id);
        if(cacheService.containsKey(hash)){
            userGroup = cacheService.get(hash);
        }
        else{
            userGroup = toDoUserGroupRepository.findById(id);
            cacheService.put(hash, userGroup);
        }
        if(userGroup.isEmpty()){
            throw new ObjectNotFoundException(id.toString());
        }
        cacheService.put(hash, userGroup);
        return userGroup.get();
    }

    public ToDoUserGroup addUserGroup(ToDoUserGroup toDoUserGroup) throws ObjectExistException {
        Optional<ToDoUserGroup> userGroup = toDoUserGroupRepository.findByName(toDoUserGroup.getGroupName());
        if(userGroup.isPresent()){
            throw new ObjectExistException(toDoUserGroup.getGroupName());
        }
        return toDoUserGroupRepository.save(toDoUserGroup);
    }

    public void updateUserNameById(Integer id, String groupName) throws BadRequestException {
        Optional<ToDoUserGroup> userGroup;
        Integer hash = Objects.hash(id);
        if(cacheService.containsKey(hash)){
            userGroup = cacheService.get(hash);
        }
        else{
            userGroup = toDoUserGroupRepository.findById(id);
            cacheService.put(hash, userGroup);
        }
        if(userGroup.isEmpty()){
            throw new BadRequestException(id.toString());
        }
        userGroup.get().setGroupName(groupName);
        if(cacheService.containsKey(hash)){
            cacheService.remove(hash);
        }
        cacheService.put(hash, userGroup);
        toDoUserGroupRepository.save(userGroup.get());
    }

    public void deleteGroupById(Integer id) throws BadRequestException {
        Integer hash = Objects.hash(id);
        Optional<ToDoUserGroup> userGroup;
        if(cacheService.containsKey(hash)){
            userGroup = cacheService.get(hash);
        }
        else{
            userGroup = toDoUserGroupRepository.findById(id);
        }
        if(userGroup.isEmpty()){
            throw new BadRequestException(id.toString());
        }
        if(cacheService.containsKey(hash)){
            cacheService.remove(hash);
        }
        toDoUserGroupRepository.deleteById(id);
    }

    @Transactional
    public ToDoUserGroup getGroupByUserId(Integer id) throws ObjectNotFoundException {
        Optional<ToDoUser> user = toDoUserRepository.findById(id);
        if(user.isEmpty()){
            throw new ObjectNotFoundException(id.toString());
        }
        Optional<ToDoUserGroup> userGroup = toDoUserGroupRepository.findGroupByUserId(id);
        if(userGroup.isEmpty()){
            throw new ObjectNotFoundException("User group nor found");
        }
        return userGroup.get();
    }

}
