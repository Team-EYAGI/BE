package com.example.eyagi.dto;


import com.example.eyagi.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
    private String username;

    public UserSearchDto(User user){
        this.username = user.getUsername();
    }

}
