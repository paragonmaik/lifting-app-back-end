package com.hoister.tonshoister.DTOs;

import com.hoister.tonshoister.models.User;

public record ProfileDTO(
    String id,
    Integer weight,
    Integer height,
    User user) {
}
