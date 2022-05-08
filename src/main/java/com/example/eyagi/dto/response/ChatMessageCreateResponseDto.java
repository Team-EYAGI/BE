package com.example.eyagi.dto.response;

import com.example.eyagi.model.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageCreateResponseDto {
    private ChatMessage.MessageType type;
    private String username;
    private String message;
    private Long roomId;
}
