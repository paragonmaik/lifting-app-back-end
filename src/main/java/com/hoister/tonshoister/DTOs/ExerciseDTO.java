package com.hoister.tonshoister.DTOs;

import java.time.LocalDateTime;

enum GoalType {
  ENDURANCE,
  HYPERTROPHY,
  STRENGTH
}

public record ExerciseDTO(
    Integer id,
    String name,
    Integer load,
    GoalType goal,
    Integer restSeconds,
    String instructions,
    LocalDateTime dateCreated) {
}
