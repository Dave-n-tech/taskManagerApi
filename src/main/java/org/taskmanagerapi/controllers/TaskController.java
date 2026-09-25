package org.taskmanagerapi.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.taskmanagerapi.dtos.request.TaskRequestDto;
import org.taskmanagerapi.models.Task;
import org.taskmanagerapi.services.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "none") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Page<Task> tasks = taskService.getAllTasks(page, size, sortBy, direction);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody TaskRequestDto taskDto) {
        Task task = taskService.createTask(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody TaskRequestDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

}
