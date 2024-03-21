package com.example.todolist.repository;

import com.example.todolist.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Integer> {
    @Query("SELECT s FROM ToDoItem s WHERE s.nameTask=:nameTask")
    Optional<ToDoItem> findByName(@Param("nameTask")String taskName);
}
