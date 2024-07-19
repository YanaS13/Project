package org.example.service;

import org.example.Enums.Category;
import org.example.Enums.Repitable;
import org.example.entity.Task;
import org.example.entity.User;
import org.example.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private TaskRepository taskRepository;

    @Autowired
    public void TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Transactional
    public Task getBaseById(int id, User user) {
        Task base = taskRepository.findByIdAndUser(id, user);
        return base;
    }
    @Transactional
    public List<Task> getAllBase(User user) {
        List<Task> bases = (List<Task>) taskRepository.findAllByUser(user);
        if (bases.size() > 0) {
            return bases;
        } else {
            return new ArrayList<Task>();
        }
    }

    @Transactional
    public Task createOrUpdateBase(Task base, User user) {
        if (base.getId() == 0) {
            base = taskRepository.save(base);
            return base;
        } else {
            Task task = taskRepository.findByIdAndUser(base.getId(), user);
            task.setId(base.getId());
            task.setTitle(base.getTitle());
            task.setDescription(base.getDescription());
            task.setDob(base.getDob());
            task.setDone(base.getDone());
            task.setPriority(base.getPriority());
            task.setCategory(base.getCategory());
            task.setRepeatable(base.getRepeatable());
            task.setUser(user);
            System.out.println(task.getId());
            taskRepository.save(task);
            return task;
        }
    }
    @Transactional
    public List<Task> findByCategory(Category category, User user) {
        return taskRepository.findAllByCategoryAndUser(category, user);
    }
    @Transactional
    public List<Task> findByName(String search, User user) {
        return taskRepository.findByTitleOrDescriptionAndUser(search, user);
    }

    @Transactional
    public void deleteBaseById(int id, User user) {
        Task base = taskRepository.findByIdAndUser(id, user);
        taskRepository.deleteById(id);
    }

    @Transactional
    public List<Task> findByActive(Boolean active, User user) {
        System.out.println(1);
        return taskRepository.findAllByDoneAndUser(active, user);
    }
    @Transactional
    public List<Task> findByTime(LocalDateTime time, User user) {
        System.out.println(2);
        return taskRepository.findByDobBeforeAndUser(time, user);
    }
    @Transactional
    public List<Task> sortByRating(User user) {
        return taskRepository.findByUserOrderByPriorityAsc(user);
    }

    @Transactional
    public void nextTime(List<Task> bases) {
        for (Task bas : bases) {
            if (bas.getRepeatable() == Repitable.DAY) {
                bas.setDob(bas.getDob().plusDays(1));
            } else if (bas.getRepeatable() == Repitable.HOURS) {
                bas.setDob(bas.getDob().plusHours(1));
            } else if (bas.getRepeatable() == Repitable.WEEK) {
                bas.setDob(bas.getDob().plusWeeks(1));
            }
            taskRepository.save(bas);
        }
    }

}
