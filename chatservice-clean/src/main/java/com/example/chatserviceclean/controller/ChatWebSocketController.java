package com.example.chatserviceclean.controller;

import com.example.chatserviceclean.dto.MessageDTO;
import com.example.chatserviceclean.service.impl.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatWebSocketController { ;
    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void receiveMessage(
            MessageDTO messageDTO) {
        chatService.sendMessageToRoom(messageDTO.roomId(), messageDTO.senderId(), messageDTO);
        log.info("Message received: {}", messageDTO);
    }

    @GetMapping("/chat/{chatId}")
    public List<MessageDTO> getMessages(
            @PathVariable Long chatId) {
        return chatService.getMessageFromRoom(chatId);
    }
}
