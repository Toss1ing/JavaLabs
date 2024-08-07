package com.example.todolist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.exception.BadRequestException;
import com.example.todolist.exception.ObjectExistException;
import com.example.todolist.exception.ObjectNotFoundException;
import com.example.todolist.model.Group;
import com.example.todolist.service.CounterService;
import com.example.todolist.service.GroupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "api/v1/todo")
@AllArgsConstructor
@Tag(name = "Group of users", description = "Direction with group of users")
public class GroupController {

    private final GroupService groupService;

    private final CounterService counterService;

    @GetMapping(path = "group")
    @Operation(summary = "Get groups", description = "Get list of groups")
    public ResponseEntity<List<Group>> gerAllUserGroup() throws ObjectNotFoundException {
        counterService.incrementCounter();
        return new ResponseEntity<>(groupService.getAllUserGroup(), HttpStatus.OK);
    }

    @GetMapping(path = "group/id/{id}")
    @Operation(summary = "Get group", description = "Get group of user by id")
    public ResponseEntity<Group> getUserGroupById(@PathVariable final Integer id) throws ObjectNotFoundException {
        counterService.incrementCounter();
        return new ResponseEntity<>(groupService.getUserGroupById(id), HttpStatus.OK);
    }

    @GetMapping(path = "group/name/{groupName}")
    public ResponseEntity<Group> getGroupByName(@PathVariable final String groupName) throws ObjectNotFoundException {
        counterService.incrementCounter();
        return new ResponseEntity<>(groupService.getGroupByName(groupName), HttpStatus.OK);
    }

    @GetMapping(path = "group/user/id/{id}")
    @Operation(summary = "Get group", description = "Get group of user by user id")
    public ResponseEntity<Group> getGroupByUserId(@PathVariable final Integer id) throws ObjectNotFoundException {
        counterService.incrementCounter();
        return new ResponseEntity<>(groupService.getGroupByUserId(id), HttpStatus.OK);
    }

    @PostMapping(path = "group/add")
    @Operation(summary = "Add group of user")
    public ResponseEntity<Group> addGroup(@RequestBody final Group toDoUserGroup)
            throws ObjectExistException {
        counterService.incrementCounter();
        return new ResponseEntity<>(groupService.addUserGroup(toDoUserGroup), HttpStatus.CREATED);
    }

    @PutMapping(path = "group/name/new/{id}/{groupName}")
    @Operation(summary = "Update group of user")
    public ResponseEntity<Group> updateGroupNameById(@PathVariable final Integer id,
            @PathVariable final String groupName) throws BadRequestException {
        counterService.incrementCounter();
        return new ResponseEntity<>(groupService.updateGroupNameById(id, groupName), HttpStatus.OK);
    }

    @DeleteMapping(path = "group/delete/{id}")
    @Operation(summary = "Delete group of user", description = "Delete group of user by id")
    public ResponseEntity<Group> deleteGroupById(@PathVariable final Integer id) throws BadRequestException {
        counterService.incrementCounter();
        return new ResponseEntity<>(groupService.deleteGroupById(id), HttpStatus.NO_CONTENT);
    }
}
