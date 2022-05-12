package com.example.eyagi.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundResponseDto {
    private Long fundId;
    private String sellerName;
    private String content;
    private int likeCnt;
    private String fundFile;
    private String bookTitle;
    private String author;
    private String bookImg;
    private boolean myHeart;
    private int fundingGoals;
    private boolean successFunding;
}
