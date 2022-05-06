package com.example.eyagi.controller;

import com.example.eyagi.dto.SellerProfileDto;
import com.example.eyagi.dto.UserProfileDto;
import com.example.eyagi.model.UserRole;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.AwsS3Path;
import com.example.eyagi.service.AwsS3Service;
import com.example.eyagi.service.UserPageService;
import com.example.eyagi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.eyagi.service.AwsS3Path.pathInfo;

@RequiredArgsConstructor
@RestController
public class UserPageController {

    private final UserPageService userPageService;
    private final UserService userService;
    private final AwsS3Service awsS3Service;




//
      //todo:마이페이지 조회 - 해야됨

    //todo:마이페이지 조회 .1 프로필 불러오기 , 판매자는 음성도 같이.
    @GetMapping("/user/profiles/load")
    public void loadUserProfile(){

    }

    //todo:마이페이지 조회 .2 내 서재 불러오기 , 서재에 담은 책 + 듣고 있는 오디오 목록
    @GetMapping("/user/profiles/load/library")
    public void loadUserLibrary(@AuthenticationPrincipal UserDetailsImpl userDetails){

    }

    //todo:마이페이지 조회 .3 판매자 전용 버튼, 내가 등록한 오디오북 + 내가 등록한 펀딩 목록
    @GetMapping("/user/profiles/load/seller")
    public void loadSellerPage(){

    }

    //todo: 일반 사용자 마이페이지에 내가 참여한 펀딩 목록이 있으면 좋을 것 같다.
    @GetMapping("/user/profiles/load/joinFund")
    public void loadUserJoinFund(){

    }


    //todo:내 서재에 책 담기 - FE 확인 전
    @PostMapping("/book/detail/{bookId}/heart")
    public ResponseEntity<String> heartBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userPageService.heartBook(userDetails.getUsername(), bookId);
        return ResponseEntity.ok("도서가 서재에 쏙 담겼습니다!");
    }

    //todo:프로필 등록 일반 사용자와 셀러를 role로 구분하여 서로 다른 메서드 실행. - FE 확인 전
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


    //todo:프로필 수정 -> 등록한 사진을 지우고 기본 이미지로 하고 싶으면 어떡할까 ? 정하고 하자 - FE 확인 전
//    @PutMapping("/user/profiles/change")
//    public ResponseEntity editUserProfile(@RequestPart(name = "image", required = false) MultipartFile file,
//                                @RequestPart(name = "info",  required = false) SellerProfileDto dto,
//                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
////        UserRole role = userDetails.getUser().getRole();
////        if(role == UserRole.SELLER) {
////            SellerProfileDto.ResponseDto sellerDto = userPageService.newProfileSeller(file, userDetails.getUsername(), dto);
////            return new ResponseEntity(sellerDto, HttpStatus.OK);
////        } else {
////            UserProfileDto userDto = userPageService.newProfile(file, userDetails.getUsername());
////            return new ResponseEntity(userDto, HttpStatus.OK);
////        }
//
//    }


    //todo:판매자 프로필 - 나의 음성 등록 -> 음성파일 크기 제한 두기.
    @PostMapping("/user/profiles/myVoice")
    public void myVoice(@RequestPart(name = "audio") MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails){
        awsS3Service.uploadFile(file, pathInfo);
    }



}
