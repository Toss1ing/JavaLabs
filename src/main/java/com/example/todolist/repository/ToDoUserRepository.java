package com.example.todolist.repository;

import com.example.todolist.model.ToDoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ToDoUserRepository extends JpaRepository<ToDoUser, Integer> {
    
    @Query("SELECT s FROM ToDoUser s WHERE s.loginName=:userName")
    Optional<ToDoUser> findByName(@Param("userName")String userName);

    @Query("SELECT s FROM ToDoUser s JOIN s.toDoItems t WHERE t.id =:itemId")
    List<ToDoUser> findUserWithTaskById(@Param("itemId") Integer itemId);

}
