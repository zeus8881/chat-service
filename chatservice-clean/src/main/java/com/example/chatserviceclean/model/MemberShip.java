package com.example.chatserviceclean.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member-ship")
public class MemberShip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatRoomId;
    private Long userId;
    private String role;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatRoom chat;
}