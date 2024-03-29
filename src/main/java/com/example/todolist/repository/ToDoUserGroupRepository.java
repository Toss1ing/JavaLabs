package com.example.todolist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.todolist.model.ToDoUserGroup;

@Repository
public interface ToDoUserGroupRepository extends JpaRepository<ToDoUserGroup,Integer> {

    @Query("SELECT s FROM ToDoUserGroup s WHERE s.groupName =: groupName")
    Optional<ToDoUserGroup> findByName(@Param("groupName")String groupName);

    @Query("SELECT s FROM ToDoUserGroup s JOIN s.toDoUsers t WHERE t.id=:userId")
	Optional<ToDoUserGroup> findGroupByUserId(@Param("userId")Integer userId);

}