package com.example.chatserviceclean.service.impl;

import com.example.chatserviceclean.dto.ChatRoomDTO;
import com.example.chatserviceclean.mapper.ChatRoomMapper;
import com.example.chatserviceclean.model.ChatRoom;
import com.example.chatserviceclean.model.MemberShip;
import com.example.chatserviceclean.repository.ChatRoomRepository;
import com.example.chatserviceclean.repository.MemberShipRepository;
import com.example.chatserviceclean.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomMapper chatRoomMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberShipRepository memberShipRepository;

    @Override
    public ResponseEntity<ChatRoomDTO> createChatRoom(ChatRoomDTO chatRoomDTO) {
        ChatRoom chatRoom = chatRoomMapper.toModel(chatRoomDTO);

        ChatRoom save = chatRoomRepository.save(chatRoom);
        ChatRoomDTO dto = chatRoomMapper.toDTO(save);

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<ChatRoomDTO> getChatRoomInfo(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat room not found"));

        ChatRoomDTO chatRoomDTO = chatRoomMapper.toDTO(chatRoom);
        return ResponseEntity.ok(chatRoomDTO);
    }

    @Override
    public ResponseEntity<List<ChatRoomDTO>> getAllRoomsByUserId(Long userId) {
        List<MemberShip> memberShip = memberShipRepository.getMemberShipByUserId(userId);

        List<Long> roomIds = memberShip.stream().map(MemberShip::getChatRoomId)
                .toList();
        List<ChatRoom> rooms = chatRoomRepository.findAllById(roomIds);

        List<ChatRoomDTO> chatRoomDTOS = rooms.stream().map(room -> new ChatRoomDTO(room.getId(), room.getName()))
                .toList();
        return ResponseEntity.ok(chatRoomDTOS);
    }

    @Override
    public void deleteChatRoom(Long id) {
        if (!chatRoomRepository.existsById(id)) {
            log.warn("Chat room already exists");
        }
        chatRoomRepository.deleteById(id);
        log.info("Chat room deleted successfully");

        ResponseEntity.noContent()
                .build();
    }
}
