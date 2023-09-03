package com.hoister.tonshoister.DTOs;

import java.time.LocalDateTime;

import java.util.Set;

public record WorkoutDTO(
    Integer id,
    String userId,
    String name,
    Integer durationMins,
    String description,
    LocalDateTime dateCreated,
    Set<ExerciseDTO> exercises) {
}
