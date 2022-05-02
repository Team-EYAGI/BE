package com.example.eyagi.controller;

import com.example.eyagi.dto.FundHeartRequestDto;
import com.example.eyagi.dto.FundHeartResponseDto;
import com.example.eyagi.dto.FundRequestDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FundController {
    private final FundService fundService;




    // 펀딩등록
    @PostMapping("/fund/new/{bookid}")
    public ResponseEntity<?> saveFund(
            @PathVariable Long bookid,
            @RequestPart("file") MultipartFile multipartFile ,
            @RequestPart("information") FundRequestDto fundRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return fundService.saveFund(bookid, multipartFile, fundRequestDto, userDetails);
    }

    // 펀딩목록
    @GetMapping("/fund")
    public ResponseEntity<?> getAllFund() {
        return fundService.getAllFund();
    }

    @PostMapping("/fund/like/{fundid}")
    public FundHeartResponseDto saveFundHeart(
            @PathVariable Long fundid,
            @RequestBody FundHeartRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return fundService.saveFundHeart(fundid, requestDto, userDetails);
    }
}
