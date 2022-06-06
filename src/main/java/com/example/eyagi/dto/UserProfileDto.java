package com.example.eyagi.dto;

import com.example.eyagi.model.UserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class UserProfileDto {

    private String userImage;  //S3 경로

    //일반유저용 생성자.
    public UserProfileDto (UserProfile userProfile) {
        this.userImage = userProfile.getUserImage();
    }


}
