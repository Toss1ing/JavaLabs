package com.example.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;
import java.util.List;



@Entity
@Data
@Getter
public class ToDoItem {
    @Id
    private Integer id;
    
    private String nameTask;
    private String description;
    private Instant createdDate;
    private Instant modifierDate;
    private Instant completionDate;
    private boolean complete;

    @ManyToMany(mappedBy = "toDoItems")
    @JsonIgnore
    private List<ToDoUser> users;

}
