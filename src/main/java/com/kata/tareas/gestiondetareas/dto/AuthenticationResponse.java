package com.kata.tareas.gestiondetareas.dto;

import lombok.*;

@Getter
public class AuthenticationResponse {
    private String token;
    private Long userId;

    public AuthenticationResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    // Getter
}

