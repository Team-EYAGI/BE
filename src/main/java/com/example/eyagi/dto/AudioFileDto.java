package com.example.eyagi.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AudioFileDto {

    private Long id;

    private String s3FileName; //S3 경로


}
