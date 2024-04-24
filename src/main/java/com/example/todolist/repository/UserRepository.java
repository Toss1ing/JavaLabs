package com.example.todolist.repository;

import com.example.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    @Query("SELECT s FROM User s WHERE s.loginName=:userName")
    Optional<User> findByName(@Param("userName")String userName);

    @Query("SELECT s FROM User s JOIN s.toDoItems t WHERE t.id =:itemId")
    List<User> findUserWithTaskById(@Param("itemId") Integer itemId);

}
