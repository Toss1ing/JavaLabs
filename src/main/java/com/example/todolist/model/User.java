package com.example.todolist.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "to_do_users")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;

        @Schema(name = "Name of user", example = "Tossing")
        private String loginName;

        @Schema(name = "Birth date", example = "05.06.2005")
        private Instant birthDate;

        @Schema(name = "Password of user")
        private String password;

        @Transient
        private String passwordConfirm;

        @Schema(name = "Email of user", example = "email@mail.ru")
        private String email;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_role", joinColumns = {
                        @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
                                        @JoinColumn(name = "role_id", referencedColumnName = "id") }

        )
        private List<Role> roles;

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

        public User() {
                this.loginName = "";
                this.birthDate = Instant.now();
                this.toDoItems = new ArrayList<>();
                this.userGroup = new Group();
                this.email = "";
                this.password = "";
        }

        public User(final String loginName, final Instant birthDate, final List<Item> toDoItems, final Group userGroup,
                        final String password,
                        final String email) {
                this.email = email;
                this.password = password;
                this.loginName = loginName;
                this.birthDate = birthDate;
                this.toDoItems = toDoItems;
                this.userGroup = userGroup;
        }

        public final void removeItem(final Integer itemId) {
                Item item = this.toDoItems.stream().filter(t -> t.getId() == itemId).findFirst().orElse(null);
                if (item != null) {
                        this.toDoItems.remove(item);
                        item.getUsers().remove(this);
                }
        }

        public final void removeRole(final Integer roleId) {
                Role role = this.roles.stream().filter(r -> r.getId() == roleId).findFirst().orElse(null);

                if (role != null) {
                        this.roles.remove(role);
                        role.getUsers().remove(this);
                }

        }
}
