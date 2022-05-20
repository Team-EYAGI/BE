package com.example.eyagi.dto;


import com.example.eyagi.model.Follow;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowDto {

    private Long id;
    private String name;
    private String img;

    public void following(Follow follow) { //사용자 목록 받기
        this.id =  follow.getFollower().getId();
        this.name = follow.getFollower().getUsername();
        this.img = follow.getFollower().getUserProfile().getUserImage();
    }

    public void follower(Follow follow) { //크리에이터 목록 받기
        this.id = follow.getFollowed().getId();
        this.name = follow.getFollowed().getUsername();
        this.img = follow.getFollowed().getUserProfile().getUserImage();
    }
}
