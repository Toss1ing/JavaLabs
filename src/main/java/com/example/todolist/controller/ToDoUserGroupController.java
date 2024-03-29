package com.example.todolist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.example.todolist.model.ToDoUserGroup;
import com.example.todolist.service.ToDoUserGroupService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "api/v1/todo")
@AllArgsConstructor
public class ToDoUserGroupController {

    ToDoUserGroupService toDoUserGroupService;

    @GetMapping(path = "group")
    public ResponseEntity<List<ToDoUserGroup>> gerAllUserGroup() throws ObjectNotFoundException{
        return new ResponseEntity<>(toDoUserGroupService.getAllUserGroup(), HttpStatus.OK);
    }

    @GetMapping(path = "group/id/{id}")
    public ResponseEntity<ToDoUserGroup> getUserGroupById(@PathVariable Integer id) throws ObjectNotFoundException{
        return new ResponseEntity<>(toDoUserGroupService.getUserGroupById(id), HttpStatus.OK);
    }
    
    @GetMapping(path = "group/user/id/{id}")
    public ResponseEntity<ToDoUserGroup> getGroupByUserId(@PathVariable Integer id) throws ObjectNotFoundException{
        return new ResponseEntity<>(toDoUserGroupService.getGroupByUserId(id), HttpStatus.OK);
    }
    
    @PostMapping(path = "group/add")
    public ResponseEntity<ToDoUserGroup> addGroup(@Validated @RequestBody ToDoUserGroup toDoUserGroup) throws ObjectExistException{
        return new ResponseEntity<>(toDoUserGroupService.addUserGroup(toDoUserGroup),HttpStatus.CREATED);
    }

    @PutMapping(path = "group/name/new/{id}")
    public ResponseEntity<HttpStatus> updateGroupNameById(@PathVariable Integer id,@Validated @RequestBody String groupName) throws BadRequestException {
        toDoUserGroupService.updateUserNameById(id, groupName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping(path = "group/delete/{id}")
    public ResponseEntity<HttpStatus> deleteGroupById(@PathVariable Integer id) throws BadRequestException {
        toDoUserGroupService.deleteGroupById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
