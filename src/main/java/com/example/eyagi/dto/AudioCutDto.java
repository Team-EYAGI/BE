package com.example.eyagi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AudioCutDto {


    private String contents;
    private String cutFileS3; //1분 미리듣기 편집 파일명
//    private String cutFileS3Url; //1분 미리듣기 편집 S3 경로
    private String localFile; //확장자 변환시 , 로컬에 저장된 파일명
    private String originName; //오디오 원본 파일명
    private String s3FileName; //오디오 원본 S3 경로

}
