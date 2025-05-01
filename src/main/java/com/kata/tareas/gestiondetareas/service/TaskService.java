package com.kata.tareas.gestiondetareas.service;

import org.springframework.stereotype.Service;

import com.kata.tareas.gestiondetareas.dto.TaskDTO;
import com.kata.tareas.gestiondetareas.exception.TaskNotFoundException;
import com.kata.tareas.gestiondetareas.model.Task;
import com.kata.tareas.gestiondetareas.model.User;
import com.kata.tareas.gestiondetareas.repository.TaskRepository;
import com.kata.tareas.gestiondetareas.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.isCompleted(),
            task.getUser().getId() // ⚠️ Incluye userId
        );
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        User user = userRepository.findById(taskDTO.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + taskDTO.getUserId()));

        return new Task(
            taskDTO.getId(),
            taskDTO.getTitle(),
            taskDTO.getDescription(),
            taskDTO.isCompleted(),
            user // ← asociación aquí
        );
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = convertToEntity(taskDTO);
        return convertToDTO(taskRepository.save(task));
    }

    public List<TaskDTO> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream().map(task -> new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted(), task.getUser().getId()))
                    .collect(Collectors.toList());
    }

    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));

        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setCompleted(taskDTO.isCompleted());

        // Si quieres permitir actualizar el usuario también (opcional):
        if (taskDTO.getUserId() != null && !taskDTO.getUserId().equals(existingTask.getUser().getId())) {
            User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + taskDTO.getUserId()));
            existingTask.setUser(user);
        }

        Task updatedTask = taskRepository.save(existingTask);
        return convertToDTO(updatedTask);
    }

    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
