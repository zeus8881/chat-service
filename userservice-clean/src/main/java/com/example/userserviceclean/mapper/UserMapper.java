package com.example.userserviceclean.mapper;

import com.example.userserviceclean.dto.UserDTO;
import com.example.userserviceclean.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", source = "user.role")
    UserDTO toDTO(User user);

    User toModel(UserDTO userDTO);
}
