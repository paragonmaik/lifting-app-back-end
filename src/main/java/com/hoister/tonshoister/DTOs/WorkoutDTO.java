package com.hoister.tonshoister.DTOs;

import java.util.Set;
import java.time.LocalDateTime;

public record WorkoutDTO(
    Integer id,
    String name,
    Integer durationMins,
    String description,
    LocalDateTime dateCreated,
    Set<ProgramDTO> programs) {
}
