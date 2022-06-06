package com.example.eyagi.model;

import com.example.eyagi.dto.request.ChatMessageRequestDto;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@AllArgsConstructor
public class ChatMessage extends Timestamped {

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private MessageType type;

    @Column
    private String roomId;

    @Column
    private Long senderId;

    @Column
    private String message;

    @Builder
    public ChatMessage(MessageType type, String roomId,  Long senderId, String message){
        this.type = type;
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto){
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.senderId = chatMessageRequestDto.getSenderId();
        this.message = chatMessageRequestDto.getMessage();
    }
}
