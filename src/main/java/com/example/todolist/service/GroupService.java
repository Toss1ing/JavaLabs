package com.example.todolist.service;

import static com.example.todolist.utilities.Constants.BAD_REQUEST_MSG;
import static com.example.todolist.utilities.Constants.NOT_FOUND_MSG;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.Group;
import com.example.todolist.model.User;
import com.example.todolist.repository.GroupRepository;
import com.example.todolist.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final CacheService<Integer, Optional<Group>> cacheService;

    private static final Integer ALL_CONTAINS = 1111;

    private void updateCacheService() {
        if (!cacheService.containsKey(ALL_CONTAINS)) {
            List<Group> userGroups = groupRepository.findAll();
            for (Group group : userGroups) {
                if (cacheService.containsKey(group.getId())) {
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
        if (userGroups.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return userGroups;
    }

    public Group getUserGroupById(final Integer id) throws ObjectNotFoundException {
        Optional<Group> userGroup;
        Integer hash = Objects.hash(id);
        if (cacheService.containsKey(hash)) {
            userGroup = cacheService.get(hash);
        } else {
            userGroup = groupRepository.findById(id);
            cacheService.put(hash, userGroup);
        }
        if (userGroup.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return userGroup.get();
    }

    public Group addUserGroup(final Group toDoUserGroup) throws ObjectExistException {
        return groupRepository.save(toDoUserGroup);
    }

    public Group updateGroupNameById(final Integer id, final String groupName) throws BadRequestException {
        Optional<Group> userGroup;
        Integer hash = Objects.hash(id);
        if (cacheService.containsKey(hash)) {
            userGroup = cacheService.get(hash);
        } else {
            userGroup = groupRepository.findById(id);
        }
        if (userGroup.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        userGroup.get().setGroupName(groupName);
        if (cacheService.containsKey(hash)) {
            cacheService.remove(hash);
        }
        cacheService.put(hash, userGroup);
        groupRepository.save(userGroup.get());
        return userGroup.get();
    }

    public Group deleteGroupById(final Integer id) throws BadRequestException {
        Integer hash = Objects.hash(id);
        Optional<Group> userGroup;
        if (cacheService.containsKey(hash)) {
            userGroup = cacheService.get(hash);
        } else {
            userGroup = groupRepository.findById(id);
        }
        if (userGroup.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        if (cacheService.containsKey(hash)) {
            cacheService.remove(hash);
        }
        groupRepository.deleteById(id);
        return userGroup.get();
    }

    @Transactional
    public Group getGroupByUserId(final Integer id) throws ObjectNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        Optional<Group> userGroup = groupRepository.findGroupByUserId(id);
        if (userGroup.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return userGroup.get();
    }

    public Group getGroupByName(final String nameGroup) throws ObjectNotFoundException {
        Optional<Group> group = groupRepository.findByName(nameGroup);
        if (group.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return group.get();
    }
}
