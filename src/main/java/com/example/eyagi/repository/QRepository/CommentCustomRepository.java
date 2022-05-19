package com.example.eyagi.repository.QRepository;

import java.time.LocalDateTime;

public interface CommentCustomRepository {
    Long getCommentId();
    String getContent();
    String getTitle();
    String getUsername();
    LocalDateTime getCreatedAt();
}
