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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserWebClient webClient;
    private final ChatRoomWebClient chatRoomWebClient;
    private final RedisTemplate<String, MessageDTO> redisTemplate;


    @Override
    public ResponseEntity<MessageDTO> createMessage(Long chatId, Long senderId, MessageDTO messageDTO) {
        if (chatId == null || senderId == null) {
            log.warn("Error. Id is null.");
            return ResponseEntity.badRequest().build();
        }
        Message message = messageMapper.toModel(messageDTO);
        message.setRoomId(chatId);
        message.setSenderId(senderId);
        message.setStatus(Status.DELIVERED);

        UserDTO userDTO = webClient.getUserById(senderId)
                .block();

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

        redisTemplate.opsForValue().set(getCachedKey(save.getId()), dto);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<MessageDTO> getMessageById(Long id) {
        String key = getCachedKey(id);
        MessageDTO cachedDTO = redisTemplate.opsForValue().get(key);

        if (cachedDTO != null) {
            return ResponseEntity.ok(cachedDTO);
        }
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "Message not found"));
        message.setStatus(Status.READ);

        UserDTO userDTO = webClient.getUserById(message.getSenderId())
                .block();

        ChatRoomDTO chatRoomDTO = chatRoomWebClient.getChatRoomById(message.getRoomId());

        MessageDTO dto = messageMapper.toDTO(message, userDTO, chatRoomDTO);
        redisTemplate.opsForValue().set(key, dto);

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<MessageDTO> getMessageByChatId(Long chatId, Integer limit, Integer offset) {
        Message message = new Message();
        Pageable pageable = PageRequest.of(limit, offset, Sort.by("createdAt").descending());
        messageRepository.findByRoomIdAndCreatedAt(chatId, message.getCreatedAt(), pageable);

        Message save = messageRepository.save(message);

        UserDTO userDTO = webClient.getUserById(message.getSenderId())
                .block();
        ChatRoomDTO chatRoomDTO = chatRoomWebClient.getChatRoomById(message.getRoomId());

        MessageDTO messageDTO = messageMapper.toDTO(save, userDTO, chatRoomDTO);
        redisTemplate.opsForValue().set(getCachedKey(save.getId()), messageDTO);

        return ResponseEntity.ok(messageDTO);
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

        UserDTO userDTO = webClient.getUserById(message.getSenderId())
                .block();
        ChatRoomDTO chatRoomDTO = chatRoomWebClient.getChatRoomById(message.getRoomId());

        Message save = messageRepository.save(message);
        MessageDTO dto = messageMapper.toDTO(save, userDTO, chatRoomDTO);
        redisTemplate.opsForValue().set(getCachedKey(save.getId()), dto);

        return ResponseEntity.ok(dto);
    }

    private String getCachedKey(Long id) {
        return "message-" + id;
    }
}
