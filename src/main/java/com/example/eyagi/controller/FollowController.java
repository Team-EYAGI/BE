package com.example.eyagi.controller;

import com.example.eyagi.dto.FollowDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.FollowService;
import com.example.eyagi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FollowController {
     private final FollowService followService;

    //팔로잉 하기 / 취소하기
    @PostMapping("/user/follow")
    public boolean Follow(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetail, @RequestParam ("username") String username){
         return followService.startFollow(userDetail,username);

    }

// 나를 팔로우한 사람 목록 불러오기
    @GetMapping("/user/follower")
    public List<FollowDto> getFollowerList(@RequestParam ("username") String username){
        return followService.getFollowerList(username);
    }

// 내가 팔로잉한 사람 목록 불러오기
    @GetMapping("/user/following")
    public List<FollowDto> getFollowingList(@RequestParam ("username") String username){
        return followService.getFollowingList(username);
    }


}
