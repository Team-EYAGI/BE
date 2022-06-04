package com.example.eyagi.dto;

import com.example.eyagi.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class SellerPageDto extends UserPageDto {

    private String sellerVoice; //s3 오디오 파일 경로
    private String introduce; //자기소개
    private int followerCnt;  // 나를 팔로우 하는 사람 수
//    private List<FollowDto> followerList = new ArrayList<>(); // 나를 팔로우 하고 있는 사람 목록

    public SellerPageDto(User user){
        this.userId = user.getId();
        this.userEmail = user.getEmail();
        this.userName = user.getUsername();
        this.userImage = user.getUserProfile().getUserImage();
        this.sellerVoice = user.getUserProfile().getS3FileName();
        this.introduce = user.getUserProfile().getIntroduce();
        this.followingCnt = user.getFollowingCnt();
        this.followerCnt = user.getFollwerCnt();

    }


}
