package com.example.eyagi.dto.request;

import com.example.eyagi.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageCreateRequestDto {
    private ChatMessage.MessageType type;
    private String username;
    private String message;
    private Long roomId;

}
