package org.example.repository;

import org.example.entity.TaskArchive;
import org.example.entity.Task;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskArchiveRepository extends JpaRepository<TaskArchive, Integer> {
    List<TaskArchive> findAllByUser(User user);
    List<TaskArchive> findByDobAndUser(LocalDateTime dob, User user);
    TaskArchive findByIdAndUser(Integer id, User user);
    @Query(value = "select u from archiveTask u where (u.title = %?1% or u.description = %?1%) and u.user = ?2")
    List<Task> findByTitleOrDescriptionAndUser(String search, User user);
}
