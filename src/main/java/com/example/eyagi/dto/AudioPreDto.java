package com.example.eyagi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AudioPreDto {

    private Long audioBookId; //오디오북 PK

    private String previewFile; //미리듣기 파일

    private String contents; //오디오북 설명

    private Long sellerId; //셀러 PK

    private String sellerName; //셀러 이름

    private String sellerImg; //셀러 프로필 이미지

    private String createdAt; //오디오북 찜 갯수


}
