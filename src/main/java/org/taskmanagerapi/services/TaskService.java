package org.taskmanagerapi.services;


import org.taskmanagerapi.dtos.request.TaskRequestDto;
import org.taskmanagerapi.models.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    Task createTask(TaskRequestDto taskDto);
    Task updateTask(Long id, TaskRequestDto taskDto);
    void deleteTask(Long id);
}
