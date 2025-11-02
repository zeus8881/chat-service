package com.example.messageserviceclean.repository;

import com.example.messageserviceclean.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    void findByRoomIdAndCreatedAt(Long roomId, LocalDateTime createdAt, Pageable pageable);

}
