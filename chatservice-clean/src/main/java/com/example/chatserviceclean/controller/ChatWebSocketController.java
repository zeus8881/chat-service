package com.example.chatserviceclean.controller;

import com.example.chatserviceclean.dto.MessageDTO;
import com.example.chatserviceclean.service.impl.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void receiveMessage(
            MessageDTO messageDTO) {
        chatService.sendMessageToRoom(messageDTO.roomId(), messageDTO.senderId(), messageDTO);
    }

    @GetMapping("/chat/{chatId}")
    public MessageDTO getMessages(
            @PathVariable Long chatId,
            @RequestParam int page,
            @RequestParam int size) {
        return chatService.getMessageFromRoom(chatId, page, size);
    }
}
