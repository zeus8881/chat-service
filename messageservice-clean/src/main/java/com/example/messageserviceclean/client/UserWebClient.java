package com.example.messageserviceclean.client;

import com.example.messageserviceclean.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class UserWebClient {
    private final WebClient webClient = WebClient.create("http://userservice-clean:8091");

    public UserDTO getUserById(Long id) {
        System.out.println("Get user by id:" + id);
        return webClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }
}
