package com.example.eyagi.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatRoomCreateResponseDto {

    private Long roomId;
    private String chatRoomName;
    private Long userId;
    private String userName;
}
