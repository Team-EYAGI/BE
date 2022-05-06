package com.example.eyagi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class KakaoUserInfoDto {
    private Long kakaoId;
    private String username;
    private String email;


}
