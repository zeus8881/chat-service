package com.example.userserviceclean.controller;

import com.example.userserviceclean.dto.UserDTO;
import com.example.userserviceclean.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerTest {
    @Container
    public static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.1");

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUser() throws Exception {
        UserDTO userDTO = new UserDTO(null, "anton", "123");
        String json = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void getUserById() throws Exception {
        UserDTO userDTO = new UserDTO(null, "anton", "123");
        String json = objectMapper.writeValueAsString(userDTO);

        var create = mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        var userId = objectMapper.readTree(create.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andReturn();
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void updateUser() throws Exception {
        UserDTO userDTO = new UserDTO(null, "anton", "123");
        String json = objectMapper.writeValueAsString(userDTO);

        var create = mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        var userId = objectMapper.readTree(create.getResponse().getContentAsString()).get("id").asLong();

        UserDTO updateUser = new UserDTO(userId, "sergey", "123");
        String updateJson = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(put("/users/update/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("sergey"))
                .andReturn();
    }
}