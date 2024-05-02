package com.example.todolist.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
import com.example.todolist.repository.GroupRepository;
import com.example.todolist.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CacheService<Integer, Optional<Group>> cacheService;

    @InjectMocks
    private GroupService groupService;

    private Group group = new Group();

    @BeforeEach
    void setUp() {
        group.setId(1);
        group.setGroupName("GroupName");
        group.setToDoUsers(null);
    }

    @Test
    void getAllUserGroupTest() throws ObjectNotFoundException {
        when(groupRepository.findAll()).thenReturn(Arrays.asList(group, group));

        List<Group> result = groupService.getAllUserGroup();

        assertEquals(group, result.get(0));
        assertEquals(2, result.size());
    }

    @Test
    void getAllUserGroupTest_Throw() throws ObjectNotFoundException {
        when(groupRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(ObjectNotFoundException.class, () -> groupService.getAllUserGroup());
    }

    @Test
    void getUserGroupByIdTest() throws ObjectNotFoundException {
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));

        Group result = groupService.getUserGroupById(1);

        assertEquals(group, result);
    }

    @Test
    void getUserGroupByIdTest_Throw() throws ObjectNotFoundException {
        when(groupRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> groupService.getUserGroupById(1));
    }

    @Test
    void addUserGroupTest() throws ObjectExistException {
        when(groupRepository.save(group)).thenReturn(group);

        Group result = groupService.addUserGroup(group);

        assertEquals(result, group);
    }

    @Test
    void deleteGroupByIdTest() throws BadRequestException {
        doNothing().when(groupRepository).deleteById(1);
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));

        Group result = groupService.deleteGroupById(1);

        assertEquals(result, group);
    }

    @Test
    void deleteGroupByIdTest_Throw() throws BadRequestException {
        when(groupRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> groupService.deleteGroupById(1));
    }

    @Test
    void addGroupTest() throws ObjectExistException {
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group result = groupService.addUserGroup(group);

        assertEquals(result, group);
    }

    @Test
    void updateGroupNameByIdTest() throws BadRequestException {
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group result = groupService.updateUserNameById(1, "newName");

        assertEquals("newName", result.getGroupName());
    }

    @Test
    void updateGroupNameByIdTest_Throw() throws BadRequestException {
        when(groupRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> groupService.updateUserNameById(1, "asdf"));
    }

}
