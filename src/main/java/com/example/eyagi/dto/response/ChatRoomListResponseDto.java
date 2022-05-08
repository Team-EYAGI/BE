package com.example.eyagi.dto.response;

import com.example.eyagi.model.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomListResponseDto {
    private final Long ownUserId;
    private final Long roomId;
    private final boolean newMessage;

    public ChatRoomListResponseDto(ChatRoom chatRoom, boolean newMessage) {
        this.ownUserId = chatRoom.getOwnUserId();
        this.roomId = chatRoom.getRoomId();
        this.newMessage = newMessage;
    }
}