package com.example.eyagi.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListAdminResponseDto {
    private Long roomId;
    private String romName;
    private String nickname;
    private String createdAt;
    private String userRole;
}
