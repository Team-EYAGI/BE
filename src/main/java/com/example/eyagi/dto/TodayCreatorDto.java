package com.example.eyagi.dto;

import com.example.eyagi.model.Follow;
import com.example.eyagi.model.Fund;
import com.example.eyagi.model.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TodayCreatorDto {

    private Long id;
    private String email;
    private String username;
    private String userImage;
    private UserRole userRole;

    private TodayCreatorDto(Builder builder){
        this.id = builder().id;
        this.email = builder().email;
        this.username = builder().username;
        this.userImage = builder().userImage;
    }



}
