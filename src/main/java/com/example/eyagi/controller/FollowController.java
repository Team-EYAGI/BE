package com.example.eyagi.controller;

import com.example.eyagi.dto.FollowDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class FollowController {
     private final FollowService followService;

    //팔로잉 하기 / 취소하기
    @PutMapping("/user/follow")
    public Map<String, Object> Follow(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetail,
                                      @RequestParam ("id") Long id){
         return followService.startFollow(userDetail.getUser(),id);
    }

// 나를 팔로우한 사람 목록 불러오기
    @GetMapping("/user/follower")
    public List<FollowDto> getFollowerList(@RequestParam ("id") Long id){
        return followService.getFollowerList(id);
    }

// 내가 팔로잉한 사람 목록 불러오기
    @GetMapping("/user/following")
    public List<FollowDto> getFollowingList (@RequestParam ("id") Long id){
        return followService.getFollowingList(id);
    }


}
