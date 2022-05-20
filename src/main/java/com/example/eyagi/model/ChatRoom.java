package com.example.eyagi.model;

import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(nullable = false)
    private String chatRoomName;

    @Column(nullable = false)
    private String uuid;

    @JsonIgnore
    @ManyToOne
    private User ownUser;


    public ChatRoom(String chatRoomName, String uuid, User user){
        this.chatRoomName = chatRoomName;
        this.uuid = uuid;
        this.ownUser = user;
    }
}