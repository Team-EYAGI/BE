package com.example.eyagi.repository;

import com.amazonaws.auth.policy.Policy;
import com.example.eyagi.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
//    Page<ChatMessage> findByRoomId(String roomId, Pageable pageable);
    List<ChatMessage> findByRoomId(String roomId);
    void deleteByRoomId(String toString);

    @Query("select cm from ChatMessage cm where cm.type = 'TALK' and cm.roomId = :roomId order by cm.id desc")
    ChatMessage findByRoomIdAndTypeLikeType_Talk(String roomId);
}
