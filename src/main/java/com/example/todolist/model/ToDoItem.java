package com.example.todolist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class ToDoItem {
    @Id
    private Integer id;

    private String nameTask;
    private String description;
    private Instant createdDate;
    private Instant modifierDate;
    private Instant completionDate;
    private boolean complete;
    private boolean priority;
}
