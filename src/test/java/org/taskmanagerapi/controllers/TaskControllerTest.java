package org.taskmanagerapi.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.taskmanagerapi.dtos.request.TaskRequestDto;
import org.taskmanagerapi.enums.TaskStatus;
import org.taskmanagerapi.exceptions.TaskNotFoundException;
import org.taskmanagerapi.models.Task;
import org.taskmanagerapi.services.TaskServiceImpl;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskServiceImpl taskService;

    @Test
    void createTask_withValidData_returnsCreated() throws Exception {
        TaskRequestDto requestDto = new TaskRequestDto("Cook dinner", "Make some pasta", "PENDING");

        Task expectedTask = new Task();
        expectedTask.setId(1L);
        expectedTask.setTitle("Cook dinner");
        expectedTask.setStatus(TaskStatus.PENDING);

        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn((expectedTask));

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Cook dinner"));
    }

    @Test
    void createTask_withInvalidData_returnsBadRequest() throws Exception {
        TaskRequestDto invalidDto = new TaskRequestDto("", "No title provided", "PENDING");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.response.title").exists());
    }

    @Test
    void getAllTasks_withDefaultPagination_returnsPagedData() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Clean room");
        Page<Task> pagedTasks = new PageImpl<>(List.of(task));

        when(taskService.getAllTasks(0, 10, "none", "ASC")).thenReturn(pagedTasks);

        mockMvc.perform(get("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Clean room"))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getTaskById_whenFound_returnsOk() throws Exception {
        Task task = new Task();
        task.setId(5L);
        task.setTitle("Go to gym");
        when(taskService.getTaskById(5L)).thenReturn(task);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.title").value("Go to gym"));
    }

    @Test
    void getTaskById_whenNotFound_returnsNotFoundStatus() throws Exception {
        when(taskService.getTaskById(99L)).thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.response.error").value("Task not found with id: 99"));
    }

    @Test
    void deleteTask_whenIdExists_returnsNoContent() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

}