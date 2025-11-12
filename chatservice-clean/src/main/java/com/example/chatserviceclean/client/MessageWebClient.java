package com.example.chatserviceclean.client;

import com.example.chatserviceclean.dto.MessageDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class MessageWebClient {

    private final WebClient webClient = WebClient.create("http://messageservice-clean:8094");

    public List<MessageDTO> getMessages(Long chatId, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/messages/by-room/{roomId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(chatId))
                .retrieve()
                .bodyToFlux(MessageDTO.class)
                .collectList()
                .block();
    }

    public MessageDTO sendMessage(Long chatId, Long senderId, MessageDTO messageDTO) {
        return webClient.post()
                .uri("/messages/{chatId}/send/{senderId}", chatId, senderId)
                .bodyValue(messageDTO)
                .retrieve()
                .bodyToMono(MessageDTO.class)
                .block();
    }
}
