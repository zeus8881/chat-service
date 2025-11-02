package com.example.messageserviceclean.service;

import com.example.messageserviceclean.dto.MessageDTO;
import org.springframework.http.ResponseEntity;

public interface MessageService {
    ResponseEntity<MessageDTO> createMessage(Long chatId, Long senderId, MessageDTO messageDTO);

    ResponseEntity<MessageDTO> getMessageById(Long id);

    ResponseEntity<MessageDTO> getMessageByChatId(Long chatId, Integer limit, Integer offset);

    ResponseEntity<Void> deleteMessage(Long id);

    ResponseEntity<MessageDTO> updateMessage(Long id, String newContent);
}
