package com.example.userserviceclean.service;

import com.example.userserviceclean.dto.UserDTO;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<UserDTO> createUser(UserDTO userDTO);

    ResponseEntity<UserDTO> getUserById(Long id);

    ResponseEntity<UserDTO> updateUser(Long userId, UserDTO userDTO);

    ResponseEntity<List<UserDTO>> getAllUsers();
}
