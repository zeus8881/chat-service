package com.example.chatserviceclean.service;

import com.example.chatserviceclean.dto.ChatRoomDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChatRoomService {
    ResponseEntity<ChatRoomDTO> createChatRoom(ChatRoomDTO chatRoomDTO);

    ResponseEntity<ChatRoomDTO> getChatRoomInfo(Long id);

    ResponseEntity<List<ChatRoomDTO>> getAllRoomsByUserId(Long userId);

    void deleteChatRoom(Long id);
}
