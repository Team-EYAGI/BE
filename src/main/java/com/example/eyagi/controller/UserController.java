package com.example.eyagi.controller;

import com.example.eyagi.dto.SignupRequestDto;
import com.example.eyagi.model.UserLibrary;
import com.example.eyagi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    //회원가입
    @PostMapping("/user/join")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequestDto signupRequestDto){
        return userService.registerUser(signupRequestDto);
    }

    //이메일 중복확인
    @PostMapping("/user/email/check")
    public String userEmailCheck (@RequestBody SignupRequestDto signupRequestDto) {
        return userService.userEmailCheck(signupRequestDto.getEmail());
    }

    //닉네임 중복확인
    @PostMapping("/user/userName/check")
    public String usernameCheck (@RequestBody SignupRequestDto signupRequestDto) {
        return userService.userNameCheck(signupRequestDto.getUsername());
    }


    // 마이페이지
//    @PostMapping("/user/mypage")
//    public ResponseEntity<UserDto.MypageDto> viewMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return userService.viewMyPage(userDetails);
//    }

//    // 팔로잉 팔로우 테스트
//    @PostMapping("/user/follow/{id}")
//    public void followUser(@RequestHeader("AccessAuthorization") String user,
//                             @PathVariable Long id) {
//        userService.addFollow(user, id);
//    }





    //팔로잉하기 .  사용자 -> 셀러
    @PostMapping("/user/follow")
    public void userFollowing(){

    }


    //언팔로잉.  팔로우취소
    @DeleteMapping("/user/unfollow")
    public void userUnfollow(){

    }

}
