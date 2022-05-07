package com.example.eyagi.dto;

import com.example.eyagi.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SellerPageDto extends UserPageDto {

    private String sellerVoice; //s3 오디오 파일 경로

    public SellerPageDto(User user){
        this.userEmail = user.getEmail();
        this.userName = user.getUsername();
        this.userImage = user.getUserProfile().getUserImage();
        this.sellerVoice = user.getUserProfile().getS3FileName();
    }


}
