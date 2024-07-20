package model;

public record RegisterUserRequest(
        String username,
        String password,
        String email
) {}
