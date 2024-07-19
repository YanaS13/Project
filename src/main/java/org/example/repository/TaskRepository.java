package org.example.repository;

import org.example.Enums.Category;
import org.example.entity.Task;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task findByIdAndUser(Integer id, User user);
    List<Task> findAllByUser(User user);
    List<Task> findByUserAndTitle(User user, String title);
    List<Task> findByUserOrderByPriorityAsc(User user);
    List<Task> findByDobBeforeAndUser(LocalDateTime dob, User user);
    @Query(value = "select u from Task u where (u.title = %?1% or u.description = %?1%) and u.user = ?2")
    List<Task> findByTitleOrDescriptionAndUser(String title, User user);
    List<Task> findAllByCategoryAndUser(Category category, User user);
    List<Task> findAllByDoneAndUser(Boolean done, User user);
}
