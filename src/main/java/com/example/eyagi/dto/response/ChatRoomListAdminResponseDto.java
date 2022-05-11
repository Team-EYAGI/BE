package com.example.eyagi.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListAdminResponseDto {
    private Long roomId;
    private String nickname;
    private String CreatedAt;
}
