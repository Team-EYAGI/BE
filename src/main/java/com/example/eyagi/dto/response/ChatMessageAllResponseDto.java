package com.example.eyagi.dto.response;

import com.example.eyagi.model.ChatMessage;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageAllResponseDto {
    private ChatMessage.MessageType type;
    private Long id;
    private String createdAt;
    private String senderNickname;
    private String message;
}
