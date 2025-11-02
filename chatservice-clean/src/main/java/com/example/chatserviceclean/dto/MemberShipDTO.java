package com.example.chatserviceclean.dto;

public record MemberShipDTO(Long id,
                            Long chatRoomId,
                            Long userId,
                            String role,
                            UserDTO userDTO) {
}
