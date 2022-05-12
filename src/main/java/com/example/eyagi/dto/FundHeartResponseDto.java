package com.example.eyagi.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FundHeartResponseDto {
    private boolean fundHeartBool;
    private int fundHeartCnt;
}
