package com.kata.tareas.gestiondetareas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kata.tareas.gestiondetareas.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
