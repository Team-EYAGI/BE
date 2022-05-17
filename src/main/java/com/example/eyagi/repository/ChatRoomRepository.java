package com.example.eyagi.repository;

import com.example.eyagi.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByRoomId(Long roomId);
    void deleteByRoomId(Long roomId);
}
