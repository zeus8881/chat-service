package com.example.messageserviceclean.controller;

import com.example.messageserviceclean.dto.MessageDTO;
import com.example.messageserviceclean.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    public final MessageService messageService;

    @PostMapping("/{chatId}/send/{senderId}")
    public ResponseEntity<MessageDTO> createMessage(
            @PathVariable Long chatId,
            @PathVariable Long senderId,
            @RequestBody MessageDTO messageDTO) {
        return messageService.createMessage(chatId, senderId, messageDTO);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<MessageDTO> getMessageById(
            @PathVariable Long id) {
        return messageService.getMessageById(id);
    }

    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<MessageDTO> getMessageByChatId(
            @PathVariable Long roomId,
            @RequestParam int page,
            @RequestParam int size) {
        return messageService.getMessageByChatId(roomId, size, page);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long id) {
        return messageService.deleteMessage(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MessageDTO> updateMessage(
            @PathVariable Long id,
            @RequestParam String newContent) {
        return messageService.updateMessage(id, newContent);
    }
}
