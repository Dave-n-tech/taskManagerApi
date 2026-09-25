package org.taskmanagerapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Task> getAllTasks(int page, int size, String sortBy, String direction) {
        Sort sort;

        if (sortBy == null || sortBy.isBlank() || sortBy.equalsIgnoreCase("none")) {
            sort = Sort.unsorted();
        } else {
            sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name())
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return taskRepository.findAll(pageable);
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
