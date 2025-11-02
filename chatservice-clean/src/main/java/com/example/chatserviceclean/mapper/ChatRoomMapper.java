package com.example.chatserviceclean.mapper;

import com.example.chatserviceclean.dto.ChatRoomDTO;
import com.example.chatserviceclean.model.ChatRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    ChatRoomDTO toDTO(ChatRoom chatRoom);

    ChatRoom toModel(ChatRoomDTO chatRoomDTO);
}
