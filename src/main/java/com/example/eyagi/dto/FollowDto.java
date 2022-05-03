package com.example.eyagi.dto;

import lombok.*;

@Builder
@Getter
@Data
public class FollowDto {
    private Long id;
    private String email;
    private String userImage;
    private int followState;
    private int loginUser;

    public FollowDto(Long id, String email, String userImage, int followState, int loginUser) {
        this.id = id;
        this.email = email;
        this.userImage = userImage;
        this.followState = followState;
        this.loginUser = loginUser;
    }
}
