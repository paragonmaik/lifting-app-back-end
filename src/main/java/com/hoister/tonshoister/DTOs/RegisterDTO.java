package com.hoister.tonshoister.DTOs;

import com.hoister.tonshoister.models.UserRole;

public record RegisterDTO(
    String login,
    String password,
    UserRole role) {
}
