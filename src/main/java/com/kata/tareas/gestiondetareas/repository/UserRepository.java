package com.kata.tareas.gestiondetareas.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.kata.tareas.gestiondetareas.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
