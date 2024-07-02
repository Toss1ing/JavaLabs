package com.example.todolist.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
import com.example.todolist.model.User;
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
    private User user = new User();

    @BeforeEach
    void setUp() {
        group.setId(1);
        group.setGroupName("GroupName");
        group.setToDoUsers(null);

        user.setId(1);
        user.setBirthDate(Instant.now());
        user.setLoginName("UserName");
        user.setToDoItems(null);
        user.setUserGroup(group);
        user.setToDoItems(new ArrayList<>());
    }

    @Test
    void getAllUserGroupTest() throws ObjectNotFoundException {
        when(groupRepository.findAll()).thenReturn(Arrays.asList(group, group));

        List<Group> result = groupService.getAllUserGroup();

        assertEquals(group, result.get(0));
        assertEquals(2, result.size());
    }

    @Test
    void getAllUserGroupTest_Throw() {
        when(groupRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(ObjectNotFoundException.class, () -> groupService.getAllUserGroup());
    }

    @Test
    void getUserGroupByIdRepositoryTest() throws ObjectNotFoundException {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(false);
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));

        Group result = groupService.getUserGroupById(1);

        assertEquals(group, result);
        verify(cacheService, times(1)).put(hash, Optional.of(group));
    }

    @Test
    void getUserGroupByIdCacheTest() throws ObjectNotFoundException {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(true);
        when(cacheService.get(hash)).thenReturn(Optional.of(group));

        Group result = groupService.getUserGroupById(1);

        assertEquals(group, result);
    }

    @Test
    void getUserGroupByIdTest_Throw() {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(false);
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
    void deleteGroupByIdRepositoryTest() throws BadRequestException {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(false);
        doNothing().when(groupRepository).deleteById(1);
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));

        Group result = groupService.deleteGroupById(1);

        assertEquals(result, group);
    }

    @Test
    void deleteGroupByIdCacheTest() throws BadRequestException {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(true);
        when(cacheService.get(hash)).thenReturn(Optional.of(group));

        Group result = groupService.deleteGroupById(1);

        assertEquals(result, group);
        verify(cacheService, times(1)).remove(hash);

    }

    @Test
    void deleteGroupByIdTest_Throw() {
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
    void updateGroupNameByIdRepositoryTest() throws BadRequestException {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(false);
        when(groupRepository.findById(1)).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group result = groupService.updateGroupNameById(1, "newName");

        assertEquals("newName", result.getGroupName());
        verify(cacheService, times(1)).put(hash, Optional.of(group));
    }

    @Test
    void updateGroupNameByIdCacheTest() throws BadRequestException {
        Integer hash = Objects.hash(1);
        when(cacheService.containsKey(hash)).thenReturn(true);
        when(cacheService.get(hash)).thenReturn(Optional.of(group));

        Group result = groupService.updateGroupNameById(1, "newName");

        assertEquals("newName", result.getGroupName());
        verify(cacheService, times(1)).remove(hash);
        verify(cacheService, times(1)).put(hash, Optional.of(group));

    }

    @Test
    void getGroupByUserIdTest() throws ObjectNotFoundException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(groupRepository.findGroupByUserId(user.getId())).thenReturn(Optional.of(group));

        Group result = groupService.getGroupByUserId(1);

        assertEquals(result, group);

    }

    @Test
    void getGroupByUserIdTest_ThrowUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> groupService.getGroupByUserId(1));

    }

    @Test
    void getGroupByUserIdTest_ThrowGroupNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(groupRepository.findGroupByUserId(user.getId())).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> groupService.getGroupByUserId(1));

    }

    @Test
    void updateGroupNameByIdTest_Throw() {
        when(groupRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> groupService.updateGroupNameById(1, "asdf"));
    }

}
