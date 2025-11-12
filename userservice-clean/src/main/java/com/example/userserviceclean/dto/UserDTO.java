package com.example.userserviceclean.dto;

public record UserDTO(Long id,
                      String username,
                      String passwordHash) {
}
