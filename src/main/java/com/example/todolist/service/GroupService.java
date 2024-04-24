package com.example.todolist.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.User;
import com.example.todolist.model.Group;
import com.example.todolist.repository.GroupRepository;
import com.example.todolist.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import static com.example.todolist.utilities.Constants.NOT_FOUND_MSG;
import static com.example.todolist.utilities.Constants.BAD_REQUEST_MSG;
import static com.example.todolist.utilities.Constants.OBJECT_EXIST_MSG;

@Service
@AllArgsConstructor
public class GroupService {
    
    GroupRepository groupRepository;

    UserRepository userRepository;
    
    CacheService<Integer, Optional<Group>> cacheService;

    private static final Integer ALL_CONTAINS = 1111;

    private void updateCacheService(){
        if(!cacheService.containsKey(ALL_CONTAINS)){
            List<Group> userGroups = groupRepository.findAll();
            for(Group group: userGroups){
                if(cacheService.containsKey(group.getId())){
                    int hash = Objects.hash(group.getId());
                    cacheService.put(hash, Optional.of(group));
                }
            }
            cacheService.put(ALL_CONTAINS, null);
        }
    }

    public List<Group> getAllUserGroup() throws ObjectNotFoundException {
        updateCacheService();
        List<Group> userGroups = groupRepository.findAll();
        if(userGroups.isEmpty()){
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return userGroups;
    }

    public Group getUserGroupById(Integer id) throws ObjectNotFoundException {
        Optional<Group> userGroup;
        Integer hash = Objects.hash(id);
        if(cacheService.containsKey(hash)){
            userGroup = cacheService.get(hash);
        }
        else{
            userGroup = groupRepository.findById(id);
            cacheService.put(hash, userGroup);
        }
        if(userGroup.isEmpty()){
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        cacheService.put(hash, userGroup);
        return userGroup.get();
    }

    public Group addUserGroup(Group toDoUserGroup) throws ObjectExistException {
        Optional<Group> userGroup = groupRepository.findByName(toDoUserGroup.getGroupName());
        if(userGroup.isPresent()){
            throw new ObjectExistException(OBJECT_EXIST_MSG);
        }
        return groupRepository.save(toDoUserGroup);
    }

    public void updateUserNameById(Integer id, String groupName) throws BadRequestException {
        Optional<Group> userGroup;
        Integer hash = Objects.hash(id);
        if(cacheService.containsKey(hash)){
            userGroup = cacheService.get(hash);
        }
        else{
            userGroup = groupRepository.findById(id);
            cacheService.put(hash, userGroup);
        }
        if(userGroup.isEmpty()){
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        userGroup.get().setGroupName(groupName);
        if(cacheService.containsKey(hash)){
            cacheService.remove(hash);
        }
        cacheService.put(hash, userGroup);
        groupRepository.save(userGroup.get());
    }

    public void deleteGroupById(Integer id) throws BadRequestException {
        Integer hash = Objects.hash(id);
        Optional<Group> userGroup;
        if(cacheService.containsKey(hash)){
            userGroup = cacheService.get(hash);
        }
        else{
            userGroup = groupRepository.findById(id);
        }
        if(userGroup.isEmpty()){
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        if(cacheService.containsKey(hash)){
            cacheService.remove(hash);
        }
        groupRepository.deleteById(id);
    }

    @Transactional
    public Group getGroupByUserId(Integer id) throws ObjectNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new ObjectNotFoundException(id.toString());
        }
        Optional<Group> userGroup = groupRepository.findGroupByUserId(id);
        if(userGroup.isEmpty()){
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return userGroup.get();
    }
}
