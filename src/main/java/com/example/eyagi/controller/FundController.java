package com.example.eyagi.controller;

import com.example.eyagi.dto.FundRequestDto;
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
    @PostMapping("/fund/new/{id}")
    public ResponseEntity<?> saveFund(
            @PathVariable Long BookId,
            @RequestPart("file") MultipartFile multipartFile ,
            @RequestPart("information") FundRequestDto fundRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return fundService.saveFund(BookId, multipartFile, fundRequestDto, userDetails);
    }

    // 펀딩목록
    @GetMapping("/fund")
    public ResponseEntity<?> getAllFund() {
        return fundService.getAllFund();
    }
}
