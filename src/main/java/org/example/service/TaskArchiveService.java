package org.example.service;

import org.example.entity.TaskArchive;
import org.example.entity.User;
import org.example.repository.TaskArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskArchiveService {
    private TaskArchiveRepository archiveRepository;

    @Autowired
    public void TaskArchiveService(TaskArchiveRepository archiveRepository) {
        this.archiveRepository = archiveRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskArchive> getAllArchive(User user) {
        List<TaskArchive> bases = (List<TaskArchive>) archiveRepository.findAllByUser(user);
        if (bases.size() > 0) {
            return bases;
        } else {
            return new ArrayList<TaskArchive>();
        }
    }

    @Transactional(readOnly = true)
    public TaskArchive getArchiveById(int id, User user) {
        return archiveRepository.findByIdAndUser(id, user);
    }

    @Transactional
    public TaskArchive createOrUpdateArchive(TaskArchive archiveTask, User user) {
            archiveTask = archiveRepository.save(archiveTask);
            return archiveTask;
    }

    @Transactional
    public void deleteTaskArchiveById(int id,User user) {
        TaskArchive base = archiveRepository.findByIdAndUser(id, user);
    }
}
