package com.example.eyagi.repository;

import com.example.eyagi.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
//    Page<ChatMessage> findByRoomId(String roomId, Pageable pageable);
    List<ChatMessage> findByRoomId(String roomId);
}
