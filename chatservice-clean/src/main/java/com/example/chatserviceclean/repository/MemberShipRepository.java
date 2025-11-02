package com.example.chatserviceclean.repository;

import com.example.chatserviceclean.model.MemberShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberShipRepository extends JpaRepository<MemberShip, Long> {
    List<MemberShip> getMemberShipByUserId(Long userId);

    List<MemberShip> findAllByChatRoomId(Long chatRoomId);

    void deleteMemberShipByUserId(Long userId);

    boolean existsMemberShipByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}
