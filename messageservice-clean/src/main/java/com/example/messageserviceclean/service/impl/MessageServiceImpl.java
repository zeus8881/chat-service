package com.example.messageserviceclean.service.impl;

import com.example.messageserviceclean.client.ChatRoomWebClient;
import com.example.messageserviceclean.client.UserWebClient;
import com.example.messageserviceclean.dto.ChatRoomDTO;
import com.example.messageserviceclean.dto.MessageDTO;
import com.example.messageserviceclean.dto.UserDTO;
import com.example.messageserviceclean.mapper.MessageMapper;
import com.example.messageserviceclean.model.Message;
import com.example.messageserviceclean.model.Status;
import com.example.messageserviceclean.repository.MessageRepository;
import com.example.messageserviceclean.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserWebClient webClient;
    private final ChatRoomWebClient chatRoomWebClient;


    @Override
    public ResponseEntity<MessageDTO> createMessage(Long chatId, Long senderId, MessageDTO messageDTO) {
        if (chatId == null || senderId == null) {
            log.warn("Error. Id is null.");
            return ResponseEntity.badRequest().build();
        }
        Message message = new Message();
        message.setRoomId(chatId);
        message.setSenderId(senderId);
        message.setCreatedAt(LocalDateTime.now());
        message.setContent(messageDTO.content());
        message.setStatus(Status.DELIVERED);

        UserDTO userDTO = webClient.getUserById(senderId);
        ChatRoomDTO chatRoomDTO = chatRoomWebClient.getChatRoomById(chatId);

        Message save = messageRepository.save(message);
        MessageDTO dto = new MessageDTO(save.getId(),
                save.getRoomId(),
                save.getSenderId(),
                save.getContent(),
                save.getStatus(),
                userDTO,
                chatRoomDTO,
                userDTO.username());

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<MessageDTO> getMessageById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Message not found"));
        message.setStatus(Status.READ);

        UserDTO userDTO = webClient.getUserById(message.getSenderId());

        ChatRoomDTO chatRoomDTO = chatRoomWebClient.getChatRoomById(message.getRoomId());

        MessageDTO dto = messageMapper.toDTO(message, userDTO, chatRoomDTO);

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<List<MessageDTO>> getMessageByChatId(Long chatId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Message> messages = messageRepository.findByRoomId(chatId, pageable);
        List<Message> messagesList = messages.getContent();
        List<MessageDTO> dtos = messagesList.stream()
                .map(msg -> {
                    UserDTO userDTO = webClient.getUserById(msg.getSenderId());
                    ChatRoomDTO chatRoomDTO = chatRoomWebClient.getChatRoomById(msg.getRoomId());
                    return messageMapper.toDTO(msg, userDTO, chatRoomDTO);
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<Void> deleteMessage(Long id) {
        if (!messageRepository.existsById(id)) {
            log.warn("Message already exists!");
        }
        messageRepository.existsById(id);

        return ResponseEntity.noContent()
                .build();
    }

    @Override
    public ResponseEntity<MessageDTO> updateMessage(Long id, String newContent) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Message not found"));
        message.setContent(newContent);

        UserDTO userDTO = webClient.getUserById(message.getSenderId());
        ChatRoomDTO chatRoomDTO = chatRoomWebClient.getChatRoomById(message.getRoomId());

        Message save = messageRepository.save(message);
        MessageDTO dto = messageMapper.toDTO(save, userDTO, chatRoomDTO);

        return ResponseEntity.ok(dto);
    }
}
