package com.example.eyagi.controller;

import com.example.eyagi.dto.SellerProfileDto;
import com.example.eyagi.dto.UserProfileDto;
import com.example.eyagi.model.Library_Books;
import com.example.eyagi.model.User;
import com.example.eyagi.model.UserLibrary;
import com.example.eyagi.model.UserRole;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.UserPageService;
import com.example.eyagi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class UserPageController {

    private final UserPageService userPageService;
    private final UserService userService;

//
//    //마이페이지 조회
//    @GetMapping("/user/profiles/load")
//    public void loadUserProfile(){

//    }

    //내 서재에 책 담기
    @PostMapping("/book/detail/{bookId}/heart")
    public ResponseEntity<String> heartBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userPageService.heartBook(userDetails.getUsername(), bookId);
        return ResponseEntity.ok("도서가 서재에 쏙 담겼습니다!");
    }

    //프로필 등록  / 파일만 오면 일반 유저 , dto도 오면 샐러
    @PostMapping("/user/profiles/update")
    public ResponseEntity newUserProfile(@RequestPart(name = "image") MultipartFile file,
                                         @RequestPart(name = "info", required = false) SellerProfileDto dto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserRole role = userDetails.getUser().getRole();
        if (role == UserRole.SELLER) {
            SellerProfileDto.ResponseDto sellerDto = userPageService.newProfileSeller(file, userDetails.getUsername(), dto);
            return new ResponseEntity(sellerDto, HttpStatus.OK);
        } else {
            UserProfileDto userDto = userPageService.newProfile(file, userDetails.getUsername());
            return new ResponseEntity(userDto, HttpStatus.OK);
        }

    }


    //프로필 수정
//    @PutMapping("/user/profiles/change")
//    public ResponseEntity editUserProfile(@RequestPart(name = "image") MultipartFile file,
//                                @RequestPart(name = "info",  required = false) SellerProfileDto dto,
//                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        UserRole role = userDetails.getUser().getRole();
//        if(role == UserRole.SELLER) {
//            SellerProfileDto.ResponseDto sellerDto = userPageService.newProfileSeller(file, userDetails.getUsername(), dto);
//            return new ResponseEntity(sellerDto, HttpStatus.OK);
//        } else {
//            UserProfileDto userDto = userPageService.newProfile(file, userDetails.getUsername());
//            return new ResponseEntity(userDto, HttpStatus.OK);
//        }
//
//    }


    //판매자 프로필 - 나의 음성 등록




}
