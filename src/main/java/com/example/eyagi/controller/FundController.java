package com.example.eyagi.controller;

import com.example.eyagi.dto.FundHeartRequestDto;
import com.example.eyagi.dto.FundHeartResponseDto;
import com.example.eyagi.dto.FundRequestDto;
import com.example.eyagi.dto.FundUserRequestDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // 펀딩목록 비로그인이 보는 //part 사용
    @GetMapping("/fund")
    public ResponseEntity<?> getAllFund(@RequestPart (name = "info", required = false)FundUserRequestDto requestDto) {
        if(requestDto != null) {
            // 회원일 때
            return fundService.getAllFund(requestDto);
        } else {
            //회원 아닐때
            return fundService.getAllFundByNoUser();
        }
    }

    // 메인화면에서 펀딩목록 보여주기
    @GetMapping("/main/fund")
    public ResponseEntity<?> getFundListToMain(){
        return fundService.mainFundList();
    }

    // 펀딩좋아요
    @PostMapping("/fund/like/{fundid}")
    public FundHeartResponseDto saveFundHeart(
            @PathVariable Long fundid,
            @RequestBody FundHeartRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return fundService.saveFundHeart(fundid, requestDto, userDetails);
    }
}
