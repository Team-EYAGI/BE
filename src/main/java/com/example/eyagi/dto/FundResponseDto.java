package com.example.eyagi.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundResponseDto {
    private Long fundId;
    private String sellerName;
    private int likeCnt;
    private String bookTitle;
    private String bookImg;
    private boolean myHeart;
    private boolean successFunding;
}
