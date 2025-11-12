package com.example.messageserviceclean.service;

import com.example.messageserviceclean.dto.MessageDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MessageService {
    ResponseEntity<MessageDTO> createMessage(Long chatId, Long senderId, MessageDTO messageDTO);

    ResponseEntity<MessageDTO> getMessageById(Long id);

    ResponseEntity<List<MessageDTO>> getMessageByChatId(Long chatId, Integer page, Integer size);

    ResponseEntity<Void> deleteMessage(Long id);

    ResponseEntity<MessageDTO> updateMessage(Long id, String newContent);
}
