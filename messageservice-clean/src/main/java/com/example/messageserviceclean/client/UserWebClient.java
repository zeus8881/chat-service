package com.example.messageserviceclean.client;

import com.example.messageserviceclean.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserWebClient {
    private final WebClient webClient = WebClient.create("http://userservice-clean:8091");

    public UserDTO getUserById(Long id) {
        try {
            log.info("Get user by id: {}", id);
            return webClient.get()
                    .uri("/users/{id}")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> {
                        log.warn("User with id {} not found", id);
                        return Mono.empty();
                    })
                    .bodyToMono(UserDTO.class)
                    .blockOptional()
                    .orElse(null);
        } catch (Exception e) {
            log.error("Failed to get user by id: {}", id, e);
        }
        return null;
    }
}
