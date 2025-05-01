package com.kata.tareas.gestiondetareas.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Long userId;
}
