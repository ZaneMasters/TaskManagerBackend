package com.kata.tareas.gestiondetareas.service;

import org.springframework.stereotype.Service;

import com.kata.tareas.gestiondetareas.dto.TaskDTO;
import com.kata.tareas.gestiondetareas.exception.TaskNotFoundException;
import com.kata.tareas.gestiondetareas.model.Task;
import com.kata.tareas.gestiondetareas.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        return new Task(taskDTO.getId(), taskDTO.getTitle(), taskDTO.getDescription(), taskDTO.isCompleted());
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = convertToEntity(taskDTO);
        return convertToDTO(taskRepository.save(task));
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Actualiza los campos de la tarea existente con los datos recibidos
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setCompleted(taskDTO.isCompleted());

        Task updatedTask = taskRepository.save(existingTask);

        // Devuelve el DTO actualizado
        return new TaskDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.isCompleted()
        );
    }

    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}

