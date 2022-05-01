package com.example.eyagi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundResponseDto {
    private Long fundId;
    private String sellerName;
    private String content;
    private Long likeCnt;
    private String fundFile;
    private String bookTitle;
    private String author;
    private String bookImg;
}
