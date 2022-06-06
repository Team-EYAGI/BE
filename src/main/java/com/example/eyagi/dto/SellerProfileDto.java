package com.example.eyagi.dto;

import com.example.eyagi.model.UserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class SellerProfileDto {

        private String introduce;  //자기 소개글 -> 판매자만. 일반 유저는 사진만!

    @NoArgsConstructor
    @Getter
    public static class ResponseDto {
        private String userImage;  //S3 경로
        private String introduce;  //자기 소개글 -> 판매자만. 일반 유저는 사진만!


        public ResponseDto (UserProfile userProfile) {
            this.userImage = userProfile.getUserImage();
            this.introduce = userProfile.getIntroduce();

        }
    }

}
