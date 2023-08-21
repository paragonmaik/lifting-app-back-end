package com.hoister.tonshoister.DTOs;

import java.time.LocalDateTime;

import com.hoister.tonshoister.models.GoalType;

public record ExerciseDTO(
    Integer id,
    String name,
    Integer load,
    GoalType goal,
    Integer restSeconds,
    String instructions,
    LocalDateTime dateCreated) {
}
