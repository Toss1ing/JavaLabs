package com.example.todolist.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Entity
@Data
@Table(name = "to_do_users")
public class ToDoUser {
    @Id
    private Integer id;
    private String loginName;
    private Instant birthDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_item",
            joinColumns = {
                    @JoinColumn(name = "user_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id",referencedColumnName = "id")
            }
    )
    private List<ToDoItem> toDoItems;


}
