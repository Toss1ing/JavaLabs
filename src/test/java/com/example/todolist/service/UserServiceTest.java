package com.example.todolist.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.todolist.cache.CacheService;
import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.Group;
import com.example.todolist.model.Item;
import com.example.todolist.model.User;
import com.example.todolist.repository.ItemRepository;
import com.example.todolist.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CacheService<Integer, Optional<User>> cacheService;

    @InjectMocks
    private UserService userService;

    private User user = new User();
    private Group group = new Group();
    private Item item = new Item();

    @BeforeEach
    void setUp() {
        group.setId(1);
        group.setGroupName("GroupName");
        group.setToDoUsers(new ArrayList<>());
        group.getToDoUsers().add(user);

        user.setId(1);
        user.setBirthDate(Instant.now());
        user.setLoginName("UserName");
        user.setToDoItems(null);
        user.setUserGroup(group);
        user.setToDoItems(new ArrayList<>());
        user.getToDoItems().add(item);

        item.setId(1);
        item.setComplete(false);
        item.setCompletionDate(Instant.now());
        item.setCreatedDate(Instant.now());
        item.setDescription("Description");
        item.setModifierDate(Instant.now());
        item.setNameTask("NameTask");
        item.setUsers(new ArrayList<>());
        item.getUsers().add(user);

    }

    @Test
    void getToDoUserTest() throws ObjectNotFoundException {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user));

        List<User> result = userService.getToDoUser();

        assertEquals(2, result.size());
        assertEquals(result.get(0), user);
    }

    @Test
    void getToDoUserTest_Throw() throws ObjectNotFoundException {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(ObjectNotFoundException.class, () -> userService.getToDoUser());
    }

    @Test
    void getToDoItemByIdTest() throws ObjectNotFoundException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getToDoUserById(1);

        assertEquals(result, user);
    }

    @Test
    void getToDoItemByIdTest_Throw() throws ObjectNotFoundException {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.getToDoUserById(1));
    }

    @Test
    void getToDoUserByName() throws ObjectNotFoundException {
        when(userRepository.findByName("UserName")).thenReturn(Optional.of(user));

        User result = userService.getToDoUserByName("UserName");

        assertEquals(result, user);
    }

    @Test
    void getToDoUserByName_Throw() throws ObjectNotFoundException {
        when(userRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.getToDoUserByName("asdf"));
    }

    @Test
    void deleteUserById() throws BadRequestException {
        doNothing().when(userRepository).deleteById(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.deleteUserById(1);

        assertEquals(result, user);
    }

    @Test
    void deleteUserById_Throw() throws BadRequestException {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.deleteUserById(2));
    }

    @Test
    void addUserTest() throws ObjectExistException {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.addUser(user);

        assertEquals(result, user);
    }

    @Test
    void addTaskInUserByIdTest() throws BadRequestException {
        when(userRepository.findByName("UserName")).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.addTaskInUserById("UserName", 1);

        assertEquals(2, result.getToDoItems().size());
        assertEquals(result.getToDoItems().get(1), item);
    }

    @Test
    void addTaskInUserByIdTest_Throw() throws BadRequestException {
        when(userRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.addTaskInUserById("asdf", 123));
    }

    @Test
    void deleteTaskByIdInUserTest() throws BadRequestException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.deleteTaskByIdInUser(1, 1);

        assertEquals(0, result.getToDoItems().size());
    }

    @Test
    void deleteTaskByIdInUserTest_Throw() throws BadRequestException {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.deleteTaskByIdInUser(1, 1));
    }

    @Test
    void updateUserNameByIdTest() throws BadRequestException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.updateUserNameById(1, "newName");

        assertEquals("newName", result.getLoginName());
    }

    @Test
    void updateUserNameByIdTest_Throw() throws BadRequestException {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.updateUserNameById(1, "newName"));
    }

    @Test
    void getUsersWithTaskByIdTest() throws ObjectNotFoundException {
        when(userRepository.findUserWithTaskById(1)).thenReturn(Arrays.asList(user, user));

        List<User> result = userService.getUsersWithTaskById(1);

        assertEquals(2, result.size());
        assertEquals(result.get(0), user);
    }

    @Test
    void getUsersWithTaskByIdTest_Throw() throws ObjectNotFoundException {
        when(userRepository.findUserWithTaskById(1)).thenReturn(Arrays.asList());

        assertThrows(ObjectNotFoundException.class, () -> userService.getUsersWithTaskById(1));
    }

    @Test
    void deleteGroupInUserByIdTest() throws BadRequestException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.deleteGroupInUserByID(1);

        assertEquals(null, result.getUserGroup());
    }

    @Test
    void deleteGroupInUserByIdTest_Throw() throws BadRequestException {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> userService.deleteGroupInUserByID(1));
    }

    @Test
    void addListOfUser() throws ObjectExistException {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        List<User> result = userService.addListOfUser(Arrays.asList(user));

        assertEquals(1, result.size());
    }
}