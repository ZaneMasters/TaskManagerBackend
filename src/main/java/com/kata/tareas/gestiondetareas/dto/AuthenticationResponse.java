package com.kata.tareas.gestiondetareas.dto;

import lombok.*;

@Getter
public class AuthenticationResponse {
    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    // Getter
}

