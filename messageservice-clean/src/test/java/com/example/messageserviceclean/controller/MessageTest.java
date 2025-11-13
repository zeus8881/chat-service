package com.example.messageserviceclean.controller;

import com.example.messageserviceclean.model.Message;
import com.example.messageserviceclean.model.Status;
import com.example.messageserviceclean.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class MessageTest {
    @Autowired
    MessageRepository messageRepository;

    @Test
    public void testSave() {
        Message msg = new Message();
        msg.setContent("Test Message");
        msg.setRoomId(1L);
        msg.setSenderId(1L);
        msg.setCreatedAt(LocalDateTime.now());
        msg.setStatus(Status.DELIVERED);
        messageRepository.save(msg);
    }
}
