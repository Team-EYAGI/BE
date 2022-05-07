package com.example.eyagi.dto;


import com.example.eyagi.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSearchDto {
    private String username;
    private Long sellerId;

    public UserSearchDto(User user){
        this.username = user.getUsername();
        this.sellerId = user.getId();
    }

}
