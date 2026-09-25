package org.taskmanagerapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.taskmanagerapi.dtos.request.TaskRequestDto;
import org.taskmanagerapi.exceptions.TaskNotFoundException;
import org.taskmanagerapi.models.Task;
import org.taskmanagerapi.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @Test
    void getTaskById_success() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("cook");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskServiceImpl.getTaskById(1L);
        assertEquals("cook", result.getTitle());
    }

    @Test
    void getTaskById_not_found() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("cook");
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.getTaskById(1L));
    }

    @Test
    void getAllTasks_success() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("cook");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("clean");

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));
        List<Task> result = taskServiceImpl.getAllTasks();
        assertEquals(2, result.size());
    }

    @Test
    void createTask_success() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("cook");

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task createdTask = taskServiceImpl.getTaskById(task.getId());

        assertEquals("cook", createdTask.getTitle());
    }

    @Test
    void createTask_not_found() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("cook");

        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.getTaskById(task.getId()));
    }

    @Test
    void updateTask_success() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("cook");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskRequestDto UpdatedTaskDto = new TaskRequestDto("Read", "Read a new book", "PENDING");
        taskServiceImpl.updateTask(1L, UpdatedTaskDto);

        assertEquals("Read", task.getTitle());
    }

    @Test
    void updateTask_null_status_remains_null() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("cook");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskRequestDto UpdatedTaskDto = new TaskRequestDto("Read", "Read a new book", null);
        taskServiceImpl.updateTask(1L, UpdatedTaskDto);

        assertNull(task.getStatus());
    }

    @Test
    void deleteTask_success() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("cook");

        when(taskRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        taskServiceImpl.deleteTask(1L);

        assertTrue(taskRepository.findById(1L).isEmpty());

    }

    @Test
    void deleteTask_not_found() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("cook");

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.deleteTask(2L));
    }
}