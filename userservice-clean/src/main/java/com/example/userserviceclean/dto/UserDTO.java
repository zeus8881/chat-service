package com.example.userserviceclean.dto;

import com.example.userserviceclean.model.Role;

public record UserDTO(Long id,
                      String username,
                      String email,
                      String passwordHash,
                      Role role) {
}
