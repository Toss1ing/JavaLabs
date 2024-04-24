package com.example.todolist.model;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "to_do_group")
public class Group {
    @Id
    @Hidden
    Integer id;

    @Schema(name = "Group name", example = "Family")
    String groupName;

    @OneToMany(mappedBy = "userGroup",orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> toDoUsers;
}
