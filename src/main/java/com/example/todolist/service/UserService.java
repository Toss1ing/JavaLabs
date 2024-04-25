package com.example.todolist.service;

import static com.example.todolist.utilities.Constants.BAD_REQUEST_MSG;
import static com.example.todolist.utilities.Constants.NOT_FOUND_MSG;
import static com.example.todolist.utilities.Constants.OBJECT_EXIST_MSG;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.Item;
import com.example.todolist.model.User;
import com.example.todolist.repository.ItemRepository;
import com.example.todolist.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final CacheService<Integer, Optional<User>> cacheService;

    private static final Integer ALL_CONTAINS = 1111;

    private void updateCacheService() {
        if (!cacheService.containsKey(ALL_CONTAINS)) {
            List<User> toDoUsers = userRepository.findAll();
            for (User user : toDoUsers) {
                if (cacheService.containsKey(user.getId())) {
                    int hash = Objects.hash(user.getId());
                    cacheService.put(hash, Optional.of(user));
                }
            }
            cacheService.put(ALL_CONTAINS, null);
        }
    }

    public List<User> getToDoUser() throws ObjectNotFoundException {
        updateCacheService();
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return users;
    }

    public User getToDoUserById(final Integer id) throws ObjectNotFoundException {
        Integer hash = Objects.hashCode(id);
        Optional<User> user;
        if (cacheService.containsKey(hash)) {
            user = cacheService.get(hash);
        } else {
            user = userRepository.findById(id);
            cacheService.put(hash, user);
        }
        if (user.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return user.get();
    }

    public User getToDoUserByName(final String userName) throws ObjectNotFoundException {
        Optional<User> user = userRepository.findByName(userName);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return user.get();
    }

    public void deleteUserById(final Integer id) throws BadRequestException {
        Optional<User> user;
        Integer hash = Objects.hash(id);
        if (cacheService.containsKey(hash)) {
            user = cacheService.get(hash);
        } else {
            user = userRepository.findById(id);
        }
        if (user.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        if (cacheService.containsKey(hash)) {
            cacheService.remove(hash);
        }
        userRepository.deleteById(id);
    }

    public void deleteAllUser() {
        cacheService.clear();
        userRepository.deleteAll();
    }

    public User addUser(final User toDoUser) throws ObjectExistException {
        Optional<User> user = userRepository.findByName(toDoUser.getLoginName());
        if (user.isPresent()) {
            throw new ObjectExistException(OBJECT_EXIST_MSG);
        }
        if (toDoUser.getToDoItems() == null) {
            toDoUser.setToDoItems(new ArrayList<>());
        }
        Integer hash = Objects.hash(toDoUser.getId());
        cacheService.put(hash, user);
        return userRepository.save(toDoUser);
    }

    @Transactional
    public User addTaskInUserById(final String userName, final Integer taskId) throws BadRequestException {
        Optional<User> user = userRepository.findByName(userName);
        if (user.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        Optional<Item> item = itemRepository.findById(taskId);
        user.get().getToDoItems().add(item.get());
        Integer hash = Objects.hash(user.get().getId());
        if (cacheService.containsKey(hash)) {
            cacheService.remove(hash);
        }
        cacheService.put(hash, user);
        return userRepository.save(user.get());
    }

    @Transactional
    public void deleteTaskByIdInUser(final Integer userId, final Integer taskId) throws BadRequestException {
        Optional<User> user;
        Integer hash = Objects.hash(userId);
        if (cacheService.containsKey(hash)) {
            user = cacheService.get(hash);
        } else {
            user = userRepository.findById(userId);
        }
        if (user.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        user.get().removeItem(taskId);
        if (cacheService.containsKey(hash)) {
            cacheService.remove(hash);
        }
        cacheService.put(hash, user);
        userRepository.save(user.get());
    }

    public void updateUserNameById(final Integer userId, final String newName) throws BadRequestException {
        Optional<User> user;
        Integer hash = Objects.hash(userId);
        if (cacheService.containsKey(hash)) {
            user = cacheService.get(hash);
        } else {
            user = userRepository.findById(userId);
        }
        if (user.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        user.get().setLoginName(newName);
        if (cacheService.containsKey(userId)) {
            cacheService.remove(userId);
        }
        cacheService.put(userId, user);
        userRepository.save(user.get());
    }

    public List<User> getUsersWithTaskById(final Integer taskId) throws ObjectNotFoundException {
        List<User> usersByTaskId = userRepository.findUserWithTaskById(taskId);
        if (usersByTaskId.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MSG);
        }
        return usersByTaskId;
    }

    public void deleteGroupInUserByID(final Integer userId) throws BadRequestException {
        Optional<User> user;
        Integer hash = Objects.hash(userId);
        if (cacheService.containsKey(hash)) {
            user = cacheService.get(hash);
        } else {
            user = userRepository.findById(userId);
        }
        if (user.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        user.get().setUserGroup(null);
        if (cacheService.containsKey(hash)) {
            cacheService.remove(hash);
        }
        cacheService.put(hash, user);
        userRepository.save(user.get());
    }

    public List<User> addListOfUser(final List<User> users) throws ObjectExistException {
        List<User> usersFromRepository = userRepository.findAll();
        for (User userFromRepository : usersFromRepository) {
            for (User newUser : users) {
                if (userFromRepository.getId().equals(newUser.getId())) {
                    throw new ObjectExistException(OBJECT_EXIST_MSG);
                }
                if (newUser.getToDoItems().isEmpty()) {
                    newUser.setToDoItems(new ArrayList<>());
                }
            }
        }
        userRepository.saveAll(users);
        return users;
    }
}
