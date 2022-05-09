package com.example.eyagi.dto.response;

import com.example.eyagi.model.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomCreateResponseDto {

    private  Long roomId;
    private String chatRoomName;
    private Long userId;

    public ChatRoomCreateResponseDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.chatRoomName = chatRoom.getChatRoomName();
        this.userId = chatRoom.getOwnUserId();
    }
}
