package com.example.todolist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.todolist.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT s FROM Item s WHERE s.nameTask=:nameTask")
    Optional<Item> findByName(@Param("nameTask") final String taskName);

    @Query("SELECT s FROM Item s WHERE s.description LIKE %:keyWord%")
    List<Item> findByDescriptionTask(@Param("keyWord") final String keyWord);
}
