package com.example.eyagi.model;

import com.example.eyagi.dto.request.ChatMessageRequestDto;
import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;

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

    @JsonIgnore
    @ManyToOne
    private User sender;

    @Column
    private String message;

    @Builder
    public ChatMessage(MessageType type, String roomId, User sender, String message){
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto, User sender){
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.sender = sender;
        this.message = chatMessageRequestDto.getMessage();
    }
}
