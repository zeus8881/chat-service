package com.example.chatserviceclean.service.impl;

import com.example.chatserviceclean.client.UserWebClient;
import com.example.chatserviceclean.dto.MemberShipDTO;
import com.example.chatserviceclean.dto.UserDTO;
import com.example.chatserviceclean.mapper.ChatRoomMapper;
import com.example.chatserviceclean.mapper.MemberShipMapper;
import com.example.chatserviceclean.model.ChatRoom;
import com.example.chatserviceclean.model.MemberShip;
import com.example.chatserviceclean.repository.ChatRoomRepository;
import com.example.chatserviceclean.repository.MemberShipRepository;
import com.example.chatserviceclean.service.MemberShipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberShipServiceImpl implements MemberShipService {
    private final MemberShipRepository memberShipRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final MemberShipMapper memberShipMapper;
    private final UserWebClient userWebClient;

    @Override
    public ResponseEntity<MemberShipDTO> addUserToRoom(Long senderId, Long roomId, MemberShipDTO memberShipDTO) {
        if (senderId == null || roomId == null) {
            log.warn("Sender id or room id is null");
            return ResponseEntity.badRequest().build();
        }
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat room not found"));
        chatRoomMapper.toDTO(chatRoom);

        MemberShip memberShip = memberShipMapper.toModel(memberShipDTO);

        memberShip.setChatRoomId(roomId);
        memberShip.setUserId(senderId);
        memberShip.setChat(chatRoom);

        log.info("Member created successfully");
        UserDTO userDTO = userWebClient.getUserById(senderId);

        if (userDTO == null) {
            log.error("User not found, cannot send message.");
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found");
        }

        MemberShip save = memberShipRepository.save(memberShip);

        MemberShipDTO dto = memberShipMapper.toDTO(save, userDTO);

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsersInRoom(Long roomId) {
        if (roomId == null) {
            log.warn("Room id is null");
            return ResponseEntity.badRequest().build();
        }
        List<MemberShip> memberShips = memberShipRepository.findAllByChatRoomId(roomId);
        List<UserDTO> userDTOS = memberShips.stream()
                .map(memberShip ->
                        userWebClient.getUserById(memberShip.getUserId()))
                .toList();
        return ResponseEntity.ok(userDTOS);
    }

    @Override
    public ResponseEntity<Void> deleteUserInRoom(Long userId, Long roomId) {
        if (userId == null || roomId == null) {
            log.warn("Sender id or room id is null.");
            return ResponseEntity.badRequest().build();
        }
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat room not found"));
        memberShipRepository.deleteMemberShipByUserId(userId);
        log.info("Member deleted successfully");
        return ResponseEntity.noContent()
                .build();
    }

    @Override
    public boolean isUserInRoom(Long userId, Long roomId) {
        return memberShipRepository.existsMemberShipByUserIdAndChatRoomId(userId, roomId);
    }
}
