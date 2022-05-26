package com.example.eyagi.dto;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundDetailResponseDto {
    private Long fundId;
    private String sellerName;
    private String sellerImg;
    private String introduce;
    private String content;
    private int likeCnt;
    private String fundFile;
    private String bookTitle;
    private String author;
    private String bookImg;
    private boolean myHeart;
    private int fundingGoals;
    private boolean successFunding;
    private int followerCnt;
    private Long bookId;
    private String category;
}
