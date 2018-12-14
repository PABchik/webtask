package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.Task;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.repository.TaskRepository;
import com.Paul.web.app.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        if (task.getTest() == null) {
            throw new BuisnessException("incorrect test id");
        }
        return taskRepository.save(task);
    }


    @Override
    public Task findById(int id) {
        return taskRepository.findById(id);
    }


    @Override
    public void deleteTask(Task task) {

        taskRepository.delete(task);
    }
}