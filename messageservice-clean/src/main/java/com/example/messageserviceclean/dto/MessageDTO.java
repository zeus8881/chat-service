package com.example.messageserviceclean.dto;

import com.example.messageserviceclean.model.Status;

public record MessageDTO(Long id,
                         Long roomId,
                         Long senderId,
                         String content,
                         Status status,
                         UserDTO userDTO,
                         ChatRoomDTO chatRoomDTO,
                         String senderName) {
}
