package com.hoister.tonshoister.DTOs;

import java.time.LocalDateTime;

public record ProgramDTO(
    Integer id,
    String name,
    Integer durationWeeks,
    String description,
    LocalDateTime dateCreated) {
}
