package com.example.todolist.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "to_do_item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Item(final String taskString, final String descriptionString, final Instant dateInstant,
            final List<User> listUser) {
        this.complete = false;
        this.completionDate = dateInstant;
        this.createdDate = Instant.now();
        this.description = descriptionString;
        this.modifierDate = Instant.now();
        this.nameTask = taskString;
        if (listUser.isEmpty()) {
            this.users = new ArrayList<>();
        }
        this.users = listUser;
    }

    public Item() {
        this.complete = false;
        this.completionDate = null;
        this.createdDate = Instant.now();
        this.description = null;
        this.modifierDate = Instant.now();
        this.nameTask = "";
        this.users = new ArrayList<>();
    }

}