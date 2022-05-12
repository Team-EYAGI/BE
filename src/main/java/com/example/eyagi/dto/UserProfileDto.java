package com.example.eyagi.dto;

import com.example.eyagi.model.UserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class UserProfileDto {

    private String userImage;  //S3 경로
//    private String originImage;  //S3 파일 이름.

    //일반유저용 생성자.
    public UserProfileDto (UserProfile userProfile) {
//        this.originImage = userProfile.getOriginImage();
        this.userImage = userProfile.getUserImage();
    }


}
