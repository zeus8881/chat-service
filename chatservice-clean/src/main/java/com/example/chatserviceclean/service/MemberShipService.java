package com.example.chatserviceclean.service;

import com.example.chatserviceclean.dto.MemberShipDTO;
import com.example.chatserviceclean.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MemberShipService {
    ResponseEntity<MemberShipDTO> addUserToRoom(Long senderId, Long roomId, MemberShipDTO memberShipDTO);

    ResponseEntity<List<UserDTO>> getAllUsersInRoom(Long roomId);

    ResponseEntity<Void> deleteUserInRoom(Long userId, Long roomId);

    boolean isUserInRoom(Long userId, Long roomId);
}
