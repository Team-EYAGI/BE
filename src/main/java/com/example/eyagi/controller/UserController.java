package com.example.eyagi.controller;

import com.example.eyagi.dto.SignupRequestDto;
import com.example.eyagi.dto.UserDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("/user/join")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequestDto signupRequestDto){
        return userService.registerUser(signupRequestDto);
    }

    // 마이페이지
    @PostMapping("/user/mypage")
    public ResponseEntity<UserDto.MypageDto> viewMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        System.out.println(userDetails.getUsername());
        return userService.viewMyPage(userDetails);
    }

    // 팔로잉 팔로우 테스트
//    @PostMapping("/user/follow/{id}")
//    public void followUser(@RequestHeader("AccessAuthorization") String user,
//                             @PathVariable Long id) {
//        userService.addFollow(user, id);
//    }
}
