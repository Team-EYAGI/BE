package com.example.eyagi.controller;

import com.example.eyagi.Interceptor.Auth;
import com.example.eyagi.dto.FundHeartRequestDto;
import com.example.eyagi.dto.FundHeartResponseDto;
import com.example.eyagi.dto.FundRequestDto;
import com.example.eyagi.dto.FundUserRequestDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.FundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;


    // 펀딩등록 + 파일 크기 5MB로 지정.
    @Auth
    @PostMapping("/fund/new/{bookid}")
    public ResponseEntity<?> saveFund(
            @PathVariable Long bookid,
            @RequestPart("file") MultipartFile multipartFile ,
            @RequestPart("information") FundRequestDto fundRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {


        if (multipartFile.getSize() > 5242880 ) {
            log.error("펀딩 등록시 파일 크기 초과. 파일 크기 : {}", multipartFile.getSize());
            throw new IllegalArgumentException("파일 크기 초과");
        }
        return fundService.saveFund(bookid, multipartFile, fundRequestDto, userDetails);
    }

    // 펀딩목록 비로그인이 보는 //part 사용
    @PostMapping("/fund")
    public ResponseEntity<?> getAllFund(@RequestPart (name = "info", required = false)FundUserRequestDto requestDto, Pageable pageable) {
        if(requestDto != null) {
            // 회원일 때
            return fundService.getAllFund(requestDto, pageable);
        } else {
            //회원 아닐때
            return fundService.getAllFundByNoUser(pageable);
        }
    }

//    // 메인화면에서 펀딩목록 보여주기
//    @GetMapping("/main/fund")
//    public ResponseEntity<?> getFundListToMain(){
//        return fundService.mainFundList();
//    }

    // 펀딩좋아요
    @PostMapping("/fund/like/{fundid}")
    public FundHeartResponseDto saveFundHeart(
            @PathVariable Long fundid,
            @RequestBody FundHeartRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return fundService.saveFundHeart(fundid, requestDto, userDetails);
    }


    // 펀딩상세보기
//    @GetMapping("/fund/detail/{fundid}")
//    public ResponseEntity<?> detailFund(
//            @PathVariable Long fundid,
//            @AuthenticationPrincipal UserDetailsImpl userDetails
//    ) {
//        return fundService.detailFund(fundid, userDetails);
//    }

//    펀딩 상세보기
    @PostMapping("/fund/detail/{fundid}")
    public ResponseEntity<?> detailFund(
            @PathVariable Long fundid,
            @RequestPart (name = "info", required = false)FundUserRequestDto requestDto
    ) {
        if(requestDto != null) {
            // 회원일 때
            return fundService.detailFund(fundid, requestDto);
        } else {
            //회원 아닐때
            return fundService.detailFundNoUser(fundid);
        }
    }
}
