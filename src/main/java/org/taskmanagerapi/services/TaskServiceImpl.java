package org.taskmanagerapi.services;

import org.springframework.stereotype.Service;
import org.taskmanagerapi.dtos.request.TaskRequestDto;
import org.taskmanagerapi.enums.TaskStatus;
import org.taskmanagerapi.exceptions.TaskNotFoundException;
import org.taskmanagerapi.models.Task;
import org.taskmanagerapi.repositories.TaskRepository;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public Task createTask(TaskRequestDto taskDto) {
        Task task = Task.builder()
                .title(taskDto.title())
                .description(taskDto.description())
                .build();
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskRequestDto taskDto) {
        Task task = getTaskById(id);
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());

        if (taskDto.status() != null) {
            task.setStatus(TaskStatus.valueOf(taskDto.status()));
        }
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
