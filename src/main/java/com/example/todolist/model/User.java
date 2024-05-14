package com.example.todolist.model;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "to_do_users")
public class User {
        @Id
        private Integer id;

        @Schema(name = "Name of user", example = "Tossing")
        private String loginName;

        @Schema(name = "Birth date", example = "05.06.2005")
        private Instant birthDate;

        @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
        @JoinTable(name = "user_item", joinColumns = {
                        @JoinColumn(name = "user_id", referencedColumnName = "id")
        }, inverseJoinColumns = {
                        @JoinColumn(name = "item_id", referencedColumnName = "id")
        })
        private List<Item> toDoItems;

        @ManyToOne
        @JoinColumn(name = "group_id")
        private Group userGroup;

        public final void removeItem(final Integer itemId) {
                Item item = this.toDoItems.stream().filter(t -> t.getId() == itemId).findFirst().orElse(null);
                if (item != null) {
                        this.toDoItems.remove(item);
                        item.getUsers().remove(this);
                }
        }
}
