package com.example.messageserviceclean.client;

import com.example.messageserviceclean.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserWebClient {
    private final WebClient webClient = WebClient.create("http://userservice-clean:8091");

    public UserDTO getUserById(Long id) {
        return webClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }
}
