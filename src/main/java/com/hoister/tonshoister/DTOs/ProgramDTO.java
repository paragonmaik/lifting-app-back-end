package com.hoister.tonshoister.DTOs;

import java.time.LocalDateTime;
import java.util.Set;

public record ProgramDTO(
    Integer id,
    String userId,
    String name,
    Integer durationWeeks,
    String description,
    LocalDateTime dateCreated,
    Set<WorkoutDTO> workouts) {
}
