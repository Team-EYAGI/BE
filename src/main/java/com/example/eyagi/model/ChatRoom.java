package com.example.eyagi.model;

import lombok.*;

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

    @Column(nullable = false)
    private Long ownUserId;


    public ChatRoom(String chatRoomName, String uuid, User user){
        this.chatRoomName = chatRoomName;
        this.uuid = uuid;
        this.ownUserId = user.getId();
    }
}