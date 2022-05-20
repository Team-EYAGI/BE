package com.example.eyagi.repository.QRepository;

import java.time.LocalDateTime;

public interface BookRequestCustomRepository {
    Long getBookRequestId();
    String getTitle();
    String getContents();
    String getUserName();
    Long getBookId();
    LocalDateTime getCreatedAt();
}
