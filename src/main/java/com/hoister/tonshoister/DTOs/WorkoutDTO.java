package com.hoister.tonshoister.DTOs;

import java.time.LocalDateTime;

public record WorkoutDTO(
    Integer id,
    String name,
    Integer durationMins,
    LocalDateTime dateCreated) {
}
