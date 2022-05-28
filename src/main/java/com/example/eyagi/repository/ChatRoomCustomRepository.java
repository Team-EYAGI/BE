package com.example.eyagi.repository;

import com.example.eyagi.model.UserRole;
import java.time.LocalDateTime;

public interface ChatRoomCustomRepository {
    Long getRoomId();
    String getUuid();
    LocalDateTime getCreatedAt();
    String getChatRoomName();
    Long getOwnUserId();
    String getOwnUserEmail();
    UserRole getUserRole();
    String getOwnUsername();
}