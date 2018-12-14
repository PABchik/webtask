package com.Paul.web.app.service;

import com.Paul.web.app.entity.Task;

public interface TaskService {

    Task save(Task task);

    Task findById(int id);

    void deleteTask(Task task);
}

