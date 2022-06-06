package com.example.eyagi.dto.request;

import com.example.eyagi.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequestDto {
    private ChatMessage.MessageType type;
    private String roomId;
    private Long senderId;
    private String message;

}
