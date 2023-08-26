package com.hoister.tonshoister.DTOs;

public record RegisterDTO(
    String login,
    String password,
    String role) {
}
