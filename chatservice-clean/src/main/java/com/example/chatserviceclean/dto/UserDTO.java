package com.example.chatserviceclean.dto;

public record UserDTO(Long id,
                      String username,
                      String email,
                      String passwordHash,
                      String role) {
}
