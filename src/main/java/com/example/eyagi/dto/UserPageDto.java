package com.example.eyagi.dto;

import com.example.eyagi.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPageDto {

    protected Long userId;
    protected String userImage;  //이미지 S3 경로
    protected String userName;   // 사용자 닉네임
    protected String userEmail;  // 사용자 이메일
//    protected List<FollowDto> followingList = new ArrayList<>(); // 내가 팔로우하고 있는 크리에이터 목록
    protected int followingCnt;

    public UserPageDto(User user){
        this.userId = user.getId();
        this.userEmail = user.getEmail();
        this.userName = user.getUsername();
        this.userImage = user.getUserProfile().getUserImage();
        this.followingCnt = user.getFollowingCnt();

//        this.followingList = user.getFollowingList();

    }
}
