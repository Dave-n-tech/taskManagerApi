package org.taskmanagerapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.taskmanagerapi.dtos.request.TaskRequestDto;
import org.taskmanagerapi.enums.TaskStatus;
import org.taskmanagerapi.exceptions.TaskNotFoundException;
import org.taskmanagerapi.models.Task;
import org.taskmanagerapi.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_not_found() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.getTaskById(1L));
    }

    @Test
    void getAllTasks_withPagination_success() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("cook");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("clean");

        Page<Task> page = new PageImpl<>(List.of(task1, task2));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Task> result = taskServiceImpl.getAllTasks(0, 10, "none", "ASC");

        assertEquals(2, result.getContent().size());
        assertEquals("cook", result.getContent().get(0).getTitle());
        assertEquals("clean", result.getContent().get(1).getTitle());
        verify(taskRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void createTask_success() {
        TaskRequestDto requestDto = new TaskRequestDto("cook", "Prepare lunch", "PENDING");
        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("cook");
        savedTask.setStatus(TaskStatus.PENDING);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        Task result = taskServiceImpl.createTask(requestDto);

        assertNotNull(result);
        assertEquals("cook", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_success() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("cook");
        task.setStatus(TaskStatus.PENDING);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskRequestDto UpdatedTaskDto = new TaskRequestDto("Read", "Read a new book", "COMPLETED");
        Task result = taskServiceImpl.updateTask(1L, UpdatedTaskDto);

        assertEquals("Read", result.getTitle());
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void deleteTask_success() {

        when(taskRepository.existsById(1L)).thenReturn(true);

        taskServiceImpl.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_not_found() {
        when(taskRepository.existsById(2L)).thenReturn(false);
        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.deleteTask(2L));
        verify(taskRepository, never()).deleteById(anyLong());
    }
}