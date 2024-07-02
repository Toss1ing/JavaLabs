package com.example.todolist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.todolist.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query("SELECT s FROM Group s WHERE s.groupName=:groupName")
    Optional<Group> findByName(@Param("groupName") final String groupName);

    @Query("SELECT s FROM Group s JOIN s.toDoUsers t WHERE t.id=:userId")
    Optional<Group> findGroupByUserId(@Param("userId") final Integer userId);
}
