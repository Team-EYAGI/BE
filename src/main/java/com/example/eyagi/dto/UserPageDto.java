package com.example.eyagi.dto;

import com.example.eyagi.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPageDto {

    protected String userImage;  //이미지 S3 경로
    protected String userName;   // 사용자 닉네임
    protected String userEmail;  // 사용자 이메일

    public UserPageDto(User user){
        this.userEmail = user.getEmail();
        this.userName = user.getUsername();
        this.userImage = user.getUserProfile().getUserImage();
    }
}
