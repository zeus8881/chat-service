package com.example.messageserviceclean.controller;

import com.example.messageserviceclean.client.ChatRoomWebClient;
import com.example.messageserviceclean.client.UserWebClient;
import com.example.messageserviceclean.dto.ChatRoomDTO;
import com.example.messageserviceclean.dto.MessageDTO;
import com.example.messageserviceclean.dto.UserDTO;
import com.example.messageserviceclean.model.Message;
import com.example.messageserviceclean.model.Status;
import com.example.messageserviceclean.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class MessageControllerTest {
    @Container
    public static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.1");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private UserWebClient userWebClient;

    @MockitoBean
    private ChatRoomWebClient chatRoomWebClient;

    @MockitoBean
    private MessageRepository messageRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @BeforeEach
    void setUp() {
        UserDTO userDTO = new UserDTO(1L, "Anton", "123", "1234567", "USER");
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO(1L, "Chat room");
        Mockito.when(userWebClient.getUserById(Mockito.anyLong())).thenReturn(userDTO);
        Mockito.when(chatRoomWebClient.getChatRoomById(Mockito.anyLong())).thenReturn(chatRoomDTO);

        Message savedMessage = new Message();
        savedMessage.setId(1L);
        savedMessage.setRoomId(1L);
        savedMessage.setSenderId(1L);
        savedMessage.setContent("new content");
        savedMessage.setStatus(Status.DELIVERED);
        savedMessage.setCreatedAt(LocalDateTime.now());
        Mockito.when(messageRepository.save(Mockito.any(Message.class))).thenReturn(savedMessage);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createMessage() throws Exception {
        MessageDTO messageDTO = new MessageDTO(null, null, null, "new content", null, null, null, null);
        String json = objectMapper.writeValueAsString(messageDTO);

        mockMvc.perform(post("/messages/" + 1L + "/send/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.roomId").value(1L))
                .andExpect(jsonPath("$.senderId").value(1L))
                .andReturn();
        Mockito.verify(messageRepository, Mockito.times(1)).save(Mockito.any(Message.class));
    }

    @Test
    void getMessageById() throws Exception {
        MessageDTO messageDTO = new MessageDTO(null, null, null, "new content", null, null, null, null);
        String json = objectMapper.writeValueAsString(messageDTO);
        mockMvc.perform(get("/messages/by-id/1")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void getMessageByChatId() throws Exception {
        MessageDTO messageDTO = new MessageDTO(null, null, null, "new content", null, null, null, null);
        String json = objectMapper.writeValueAsString(messageDTO);
        mockMvc.perform(get("/messages/by-room/1")
                        .content(json))
                .andExpect(jsonPath("$.roomId").value(1L))
                .andReturn();
    }

    @Test
    void deleteMessage() throws Exception {
        MessageDTO messageDTO = new MessageDTO(null, null, null, "new content", null, null, null, null);
        String json = objectMapper.writeValueAsString(messageDTO);
        mockMvc.perform(delete("/messages/delete/1")
                        .content(json))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void updateMessage() throws Exception {
        MessageDTO messageDTO = new MessageDTO(null, null, null, "new content", null, null, null, null);
        String json = objectMapper.writeValueAsString(messageDTO);
        mockMvc.perform(put("/messages/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.content").value("new content"))
                .andReturn();

        MessageDTO newContent = new MessageDTO(null, null, null, "new content2", null, null, null, null);
        String neJson = objectMapper.writeValueAsString(newContent);

        mockMvc.perform(put("/messages/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(neJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.content").value("new content2"))
                .andReturn();
    }
}