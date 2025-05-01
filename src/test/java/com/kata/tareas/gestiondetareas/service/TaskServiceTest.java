package com.kata.tareas.gestiondetareas.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.kata.tareas.gestiondetareas.dto.TaskDTO;
import com.kata.tareas.gestiondetareas.exception.TaskNotFoundException;
import com.kata.tareas.gestiondetareas.model.Task;
import com.kata.tareas.gestiondetareas.repository.TaskRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        TaskDTO taskDTO = new TaskDTO(null, "Test Task", "Description", false, null);
        Task savedTask = new Task(1L, "Test Task", "Description", false, null);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskDTO result = taskService.createTask(taskDTO);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Description", result.getDescription());
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task(1L, "Task 1", "Description 1", false, null);
        Task task2 = new Task(2L, "Task 2", "Description 2", true, null);

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<TaskDTO> tasks = taskService.getTasksByUserId(task1.getUser().getId());

        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());
    }

    @Test
    void testDeleteTaskById_TaskExists() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);

        assertDoesNotThrow(() -> taskService.deleteTaskById(taskId));

        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void testDeleteTaskById_TaskDoesNotExist() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(taskId));
    }
}

