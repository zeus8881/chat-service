package com.example.chatserviceclean.controller;

import com.example.chatserviceclean.dto.MemberShipDTO;
import com.example.chatserviceclean.dto.UserDTO;
import com.example.chatserviceclean.service.MemberShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberShipController {
    private final MemberShipService memberShipService;

    @PostMapping("/create/{userId}/{roomId}")
    public ResponseEntity<MemberShipDTO> createMemberShip(
            @PathVariable Long userId,
            @PathVariable Long roomId,
            @RequestBody MemberShipDTO memberShipDTO) {
        return memberShipService.addUserToRoom(userId, roomId, memberShipDTO);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<UserDTO>> getAllUsersInRoom(
            @PathVariable Long roomId) {
        return memberShipService.getAllUsersInRoom(roomId);
    }

    @DeleteMapping("/delete/{userId}/{roomId}")
    public ResponseEntity<Void> deleteUserInRoom(
            @PathVariable Long userId,
            @PathVariable Long roomId) {
        return memberShipService.deleteUserInRoom(userId, roomId);
    }

    @GetMapping("/isUserInRoom?{userId}/{roomId}")
    public ResponseEntity<Boolean> isUserInRoom(
            @PathVariable Long userId,
            @PathVariable Long roomId) {
        return ResponseEntity.ok(memberShipService.isUserInRoom(userId, roomId));
    }
}
