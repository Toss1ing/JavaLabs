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
import com.example.todolist.model.User;
import com.example.todolist.service.GroupService;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTest {
    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    private Group group = new Group();

    private User user = new User();

    @BeforeEach
    public void setUp() {
        user.setId(1);
        user.setBirthDate(Instant.now());
        user.setLoginName("NewLogin");
        user.setToDoItems(null);
        user.setUserGroup(group);

        group.setId(2);
        group.setGroupName("Name");
        group.setToDoUsers(new ArrayList<>());
        group.getToDoUsers().add(user);
    }

    @Test
    @SuppressWarnings("null")
    public void getAllUserGroupTest() throws ObjectNotFoundException {
        when(groupService.getAllUserGroup()).thenReturn(Arrays.asList(group, group, group));

        ResponseEntity<List<Group>> result = groupController.gerAllUserGroup();

        assertEquals(3, result.getBody().size());
        assertEquals(group, result.getBody().get(0));
    }

    @Test
    public void getUserGroupByIdTest() throws ObjectNotFoundException {
        when(groupService.getUserGroupById(1)).thenReturn(group);

        ResponseEntity<Group> result = groupController.getUserGroupById(1);

        assertEquals(group, result.getBody());
    }

    @Test
    public void getGroupByUserIdTest() throws ObjectNotFoundException {
        when(groupService.getGroupByUserId(1)).thenReturn(group);

        ResponseEntity<Group> result = groupController.getGroupByUserId(1);

        assertEquals(group, result.getBody());
    }

    @Test
    public void addGroupTest() throws ObjectExistException {
        when(groupService.addUserGroup(group)).thenReturn(group);

        ResponseEntity<Group> result = groupController.addGroup(group);

        assertEquals(group, result.getBody());
    }

    @Test
    public void updateUserNameByIdTest() throws BadRequestException {
        ResponseEntity<Group> result = groupController.updateGroupNameById(1, "newName");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(groupService).updateUserNameById(1, "newName");
    }

    @Test
    public void deleteGroupByIdTest() throws BadRequestException {
        ResponseEntity<Group> result = groupController.deleteGroupById(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(groupService).deleteGroupById(1);
    }

}
