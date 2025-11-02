package com.example.chatserviceclean.service.impl;

import com.example.chatserviceclean.client.MessageWebClient;
import com.example.chatserviceclean.client.UserWebClient;
import com.example.chatserviceclean.dto.MessageDTO;
import com.example.chatserviceclean.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageWebClient messageWebClient;
    private final UserWebClient userWebClient;

    public void sendMessageToRoom(Long roomId, Long senderId, MessageDTO messageDTO) {
        UserDTO userDTO = userWebClient.getUserById(senderId);
        MessageDTO dtoWithSender = new MessageDTO(messageDTO.id(),
                messageDTO.roomId(),
                senderId,
                messageDTO.content(),
                messageDTO.createdAt(),
                userDTO.name());
        MessageDTO saved = messageWebClient.sendMessage(roomId, senderId, dtoWithSender);

        simpMessagingTemplate.convertAndSend("/topic/room" + roomId, saved);
    }

    public MessageDTO getMessageFromRoom(Long chatId, int page, int size) {
        return messageWebClient.getMessages(chatId, page, size);
    }
}
