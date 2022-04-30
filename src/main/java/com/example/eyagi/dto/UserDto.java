package com.example.eyagi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class MypageDto {
        private String email;
        private String username;
        private String userimgurl;
        private String userimgname;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class DeleteDto {
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class photoDto {
        private String email;
        private String username;
        private String userimgurl;
        private String userimgname;
    }
}