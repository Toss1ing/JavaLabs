package com.example.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Entity
@Data
@Table(name = "to_do_item")
public class Item {
    @Id
    @Hidden
    private Integer id;
    
    @Schema(name = "Name of task", example = "Feed dog")
    private String nameTask;

    @Schema(name = "Description of task", example = "Ima need to feed my dog, one bowl of fodder")
    private String description;

    @Schema(name = "Data of create task", example = "05.06.2005")
    private Instant createdDate;

    @Schema(name = "Data of modified task", example = "05.06.2005")
    private Instant modifierDate;

    @Schema(name = "Data of complete task", example = "05.06.2005")
    private Instant completionDate;

    @Schema(name = "Completion of task", example = "true")
    private boolean complete;

    @ManyToMany(mappedBy = "toDoItems")
    @JsonIgnore
    private List<User> users;

}
