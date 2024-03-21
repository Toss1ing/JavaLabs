package com.example.todolist.repository;

import com.example.todolist.model.ToDoUser;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ToDoUserRepository extends JpaRepository<ToDoUser, Integer> {

    @Query("SELECT S FROM ToDoUser S WHERE S.loginName=:userName")
    Optional<ToDoUser> findByName(@Param("userName")String userName);
}
