package com.example.messageserviceclean.client;

import com.example.messageserviceclean.dto.ChatRoomDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ChatRoomWebClient {
    private final WebClient webClient = WebClient.create("http://chatservice-clean:8096");

    public ChatRoomDTO getChatRoomById(Long id) {
        return webClient.get()
                .uri("/room/{id}", id)
                .retrieve()
                .bodyToMono(ChatRoomDTO.class)
                .block();
    }
}
