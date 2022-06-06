package com.example.eyagi.dto;


import com.example.eyagi.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSearchDto {
    private String username;
    private Long sellerId;
    private String sellerImg;

    public UserSearchDto(User user){
        this.username = user.getUsername();
        this.sellerId = user.getId();
        this.sellerImg = user.getUserProfile().getUserImage();
    }

}
