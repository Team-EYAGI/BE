package com.example.eyagi.controller;

import com.example.eyagi.dto.*;
import com.example.eyagi.model.User;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserProfileController {


    private final UserPageService userPageService;
    private final UserService userService;


    //todo:프로필 등록 - 일반 사용자와 셀러를 role로 구분하여 서로 다른 메서드 실행. - FE 확인 전
    @PostMapping("/user/new/profiles")
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
    @PostMapping("/seller/new/voice")
    public ResponseEntity myVoice(@RequestPart(name = "audio") MultipartFile file,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        if (userDetails.getUser().getRole()!=UserRole.SELLER){
            throw new IllegalArgumentException("오디오 등록에 권한이 없습니다.");
        }
        userPageService.sellerMyVoice(file, userDetails.getUser());
        return ResponseEntity.ok("voice 등록 완료!");
    }

    //todo : 셀러 프로필 보기.  회원, 비회원 모두 볼 수 있음.
    @GetMapping("/viewer/seller/{sellerId}")
    public SellerPageDto sellerProfileViewer (@PathVariable Long sellerId) {
        return userPageService.loadSellerPage( userService.findUserId(sellerId));
    }


    //todo : 셀러 프로필 보기 - 오디오북 목록
    @GetMapping("/viewer/sellerAudioBook/{sellerId}")
    public List<SellerAudioBook> sellerAudioBookViewer (@PathVariable Long sellerId) {
        User seller = userService.findUserId(sellerId);
        return userPageService.sellerMyAudioBook(seller);
    }

    //todo : 셀러 프로필 보기 - 펀딩 목록
    @GetMapping("/viewer/sellerFund/{sellerId}")
    public List<SellerFundDto> sellerFundViewer (@PathVariable Long sellerId) {
        User seller = userService.findUserId(sellerId);
        return userPageService.myFund(seller);
    }
}
