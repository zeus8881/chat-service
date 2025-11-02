package com.example.chatserviceclean.controller;

import com.example.chatserviceclean.dto.ChatRoomDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ChatRoomControllerTest {
    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.1");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @Test
    void createRoom() throws Exception {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO(null, "room 1");
        String json = objectMapper.writeValueAsString(chatRoomDTO);

        mockMvc.perform(post("/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void getChatRoomInfo() throws Exception {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO(null, "room 1");
        String json = objectMapper.writeValueAsString(chatRoomDTO);

        var create = mockMvc.perform(post("/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        var roomId = objectMapper.readTree(create.getResponse().getContentAsString()).get("id").asLong();
        mockMvc.perform(get("/room/" + roomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roomId))
                .andReturn();
    }

    @Test
    void getAllRoomsByUserId() throws Exception {
    }

    @Test
    void deleteChatRoom() throws Exception {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO(null, "room 1");
//        String json = objectMapper.writeValueAsString(chatRoomDTO);
//
//        var create = mockMvc.perform(post("/room/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        var roomId = objectMapper.readTree(create.getResponse().getContentAsString()).get("id").asLong();
//        mockMvc.perform(delete("/room/delete/" + roomId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent())
//                .andReturn();
    }
}