package com.example.todolist.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "to_do_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Schema(name = "Group name", example = "Family")
    private String groupName;

    @OneToMany(mappedBy = "userGroup", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> toDoUsers;

    public Group() {
        this.groupName = "";
        this.toDoUsers = new ArrayList<>();
    }

    public Group(final String nameGroup, final List<User> toDoUsers) {
        this.groupName = nameGroup;
        if (toDoUsers.isEmpty()) {
            this.toDoUsers = new ArrayList<>();
        }
        this.toDoUsers = toDoUsers;
    }
}
