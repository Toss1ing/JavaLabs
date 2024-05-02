package com.example.todolist.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.Group;
import com.example.todolist.model.Item;
import com.example.todolist.model.User;
import com.example.todolist.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

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
    @SuppressWarnings("null")
    void getToDoUserTest() throws ObjectNotFoundException {
        when(userService.getToDoUser()).thenReturn(Arrays.asList(user, user));

        ResponseEntity<List<User>> result = userController.getToDoUser();

        assertEquals(user, result.getBody().get(0));
        assertEquals(2, result.getBody().size());

    }

    @Test
    void getToDoUserByIdTest() throws ObjectNotFoundException {
        when(userService.getToDoUserById(1)).thenReturn(user);

        ResponseEntity<User> result = userController.getToDoUserById(1);

        assertEquals(user, result.getBody());
    }

    @Test
    void getToDoUserByNameTest() throws ObjectNotFoundException {
        when(userService.getToDoUserByName("UserName")).thenReturn(user);

        ResponseEntity<User> result = userController.getToDoUserByName("UserName");

        assertEquals(user, result.getBody());
    }

    @Test
    @SuppressWarnings("null")
    void getToDoUsersWithTaskByIdTest() throws ObjectNotFoundException {
        when(userService.getUsersWithTaskById(1)).thenReturn(Arrays.asList(user));

        ResponseEntity<List<User>> result = userController.getToDoUsersWithTaskById(1);

        assertEquals(user, result.getBody().get(0));
        assertEquals(1, result.getBody().size());
    }

    @Test
    void addUserTest() throws ObjectExistException {
        when(userService.addUser(user)).thenReturn(user);

        ResponseEntity<User> result = userController.addUser(user);

        assertEquals(user, result.getBody());
    }

    @Test
    void addTaskByIdInUserTest() throws BadRequestException {
        when(userService.addTaskInUserById("UserName", 1)).thenReturn(user);

        ResponseEntity<User> result = userController.addTaskByIdInUser("UserName", 1);

        assertEquals(user, result.getBody());
    }

    @Test
    void deleteUserByIdTest() throws BadRequestException {
        ResponseEntity<User> result = userController.deleteUserById(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userService).deleteUserById(1);
    }

    @Test
    void deleteAllUserTest() {
        ResponseEntity<HttpStatus> result = userController.deleteAllUser();

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userService).deleteAllUser();
    }

    @Test
    void deleteTaskInUserByIdTest() throws BadRequestException {
        ResponseEntity<User> result = userController.deleteTaskInUserById(1, 1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userService).deleteTaskByIdInUser(1, 1);
    }

    @Test
    void deleteGroupInUserByIdTest() throws BadRequestException {
        ResponseEntity<User> result = userController.deleteGroupInUserById(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userService).deleteGroupInUserByID(1);
    }

    @Test
    void updateUserNameByIdTest() throws BadRequestException {
        ResponseEntity<User> result = userController.updateUserNameById(1, "NewUserName");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userService).updateUserNameById(1, "NewUserName");
    }

    @Test
    @SuppressWarnings("null")
    void addListOfUserTest() throws ObjectExistException {
        when(userService.addListOfUser(Arrays.asList(user, user))).thenReturn(Arrays.asList(user, user));
        ResponseEntity<List<User>> result = userController.addListOfUser(Arrays.asList(user, user));

        assertEquals(user, result.getBody().get(0));
        assertEquals(2, result.getBody().size());
    }
}
