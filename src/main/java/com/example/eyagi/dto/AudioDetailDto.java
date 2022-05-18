package com.example.eyagi.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AudioDetailDto {

    private String title;  //도서명

    private String bookImg;  //책 표지

    private String author;  //저자

    private Long sellerId; //셀러 pk

    private String sellerName; //셀러 닉네임

    private String sellerImage; //샐러 프로필 이미지

    private int followingCnt; //셀러가 팔로우 하는 사람 수

    private int followerCnt; // 셀러를 팔로잉 하는 사람 수

    private String audioInfo;   // 오디오북 소개글

    private List<AudioFileDto> audioFileDtoList;  //오디오 파일 목록

    @Getter
    public static class Request {
        private String contents;
    }



}
