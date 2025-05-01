package com.kata.tareas.gestiondetareas.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.tareas.gestiondetareas.dto.TaskDTO;
import com.kata.tareas.gestiondetareas.exception.TaskNotFoundException;
import com.kata.tareas.gestiondetareas.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO(null, "Test Task", "Description", false, 10L); // userId añadido
        TaskDTO createdTask = new TaskDTO(1L, "Test Task", "Description", false, 10L);

        when(taskService.createTask(any(TaskDTO.class))).thenReturn(createdTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.userId").value(10));
    }

    @Test
    void testGetAllTasks() throws Exception {
        TaskDTO task1 = new TaskDTO(1L, "Task 1", "Description 1", false, 10L);
        TaskDTO task2 = new TaskDTO(2L, "Task 2", "Description 2", true, 10L);

        when(taskService.getTasksByUserId(task1.getUserId())).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void testDeleteTask_Success() throws Exception {
        Long taskId = 1L;

        // No exception expected
        doNothing().when(taskService).deleteTaskById(taskId);

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteTask_NotFound() throws Exception {
        Long taskId = 1L;

        doThrow(new TaskNotFoundException(taskId)).when(taskService).deleteTaskById(taskId);

        mockMvc.perform(delete("/api/tasks/{id}", taskId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with ID 1 not found."));
    }
}


