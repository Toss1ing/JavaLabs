package com.example.todolist.repository;

import com.example.todolist.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    
    @Query("SELECT s FROM Item s WHERE s.nameTask=:nameTask")
    Optional<Item> findByName(@Param("nameTask")String taskName);
 
    @Query("SELECT s FROM Item s WHERE s.description LIKE %:keyWord%")
    List<Item> findByDescriptionTask(@Param("keyWord") String keyWord);
}
