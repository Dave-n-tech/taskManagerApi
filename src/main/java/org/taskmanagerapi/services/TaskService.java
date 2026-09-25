package org.taskmanagerapi.services;


import org.springframework.data.domain.Page;
import org.taskmanagerapi.dtos.request.TaskRequestDto;
import org.taskmanagerapi.models.Task;


public interface TaskService {
    Page<Task> getAllTasks(int page, int size, String sortBy, String direction);
    Task getTaskById(Long id);
    Task createTask(TaskRequestDto taskDto);
    Task updateTask(Long id, TaskRequestDto taskDto);
    void deleteTask(Long id);
}
