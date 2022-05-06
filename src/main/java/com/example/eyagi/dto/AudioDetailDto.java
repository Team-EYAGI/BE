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

    private String sellerName; //셀러 닉네임

    private String audioInfo;   // 오디오북 소개글

    private List<AudioFileDto> audioFileDtoList;  //오디오 파일 목록

    @Getter
    public static class Request {
        private String contents;
    }



}
