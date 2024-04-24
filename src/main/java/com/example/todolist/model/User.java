package com.example.todolist.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Data
@Table(name = "to_do_users")
public class User {
    @Id
    @Hidden
    private Integer id;
    
    @Schema(name = "Name of user", example = "Tossing")
    private String loginName;

    @Schema(name = "Birth date", example = "05.06.2005")
    private Instant birthDate;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_item",
            joinColumns = {
                    @JoinColumn(name = "user_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "item_id",referencedColumnName = "id")
            }
    )
    private List<Item> toDoItems;


    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group userGroup;

    public void removeItem(Integer itemId){
        Item item = this.toDoItems.stream().filter(t -> t.getId() == itemId).findFirst().orElse(null);
        if(item != null){
                this.toDoItems.remove(item);
                item.getUsers().remove(this);
        }
    }
}

