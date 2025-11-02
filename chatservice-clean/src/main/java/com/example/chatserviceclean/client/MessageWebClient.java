package com.example.chatserviceclean.client;

import com.example.chatserviceclean.dto.MessageDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MessageWebClient {
    private final WebClient webClient = WebClient.create("http://messageservice-clean:8094");

    public MessageDTO getMessages(Long chatId, int page, int size) {
        return webClient.get()
                .uri("/{chatId}", chatId, page, size)
                .retrieve()
                .bodyToMono(MessageDTO.class)
                .block();
    }

    public MessageDTO sendMessage(Long chatId, Long senderId, MessageDTO messageDTO) {
        return webClient.post()
                .uri("/{chatId}/send/{senderId}", chatId, senderId)
                .bodyValue(messageDTO)
                .retrieve()
                .bodyToMono(MessageDTO.class)
                .block();
    }
}
