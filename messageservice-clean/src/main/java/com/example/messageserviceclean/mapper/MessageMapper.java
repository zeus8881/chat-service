package com.example.messageserviceclean.mapper;

import com.example.messageserviceclean.dto.ChatRoomDTO;
import com.example.messageserviceclean.dto.MessageDTO;
import com.example.messageserviceclean.dto.UserDTO;
import com.example.messageserviceclean.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "id", source = "message.id")
    @Mapping(target = "status", source = "message.status")
    @Mapping(target = "content", source = "message.content")
    @Mapping(target = "senderId", source = "message.senderId")
    @Mapping(target = "roomId", source = "message.roomId")
    @Mapping(target = "senderName", source = "userDTO.username")
    @Mapping(target = "chatRoomDTO", source = "chatRoomDTO")
    MessageDTO toDTO(Message message, UserDTO userDTO, ChatRoomDTO chatRoomDTO);

    Message toModel(MessageDTO messageDTO);
}
