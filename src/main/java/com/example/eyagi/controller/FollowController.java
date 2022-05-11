package com.example.eyagi.controller;

import com.example.eyagi.model.Follow;
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
     private final UserService userService;

    //팔로잉 하기 ~
    @PostMapping("/user/follow")
    public boolean Follow(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetail, @RequestParam ("email") String email){
         return followService.startFollow(userDetail,email);

    }
/* // 내가 팔로우한 사람 리스트
    @GetMapping("/user/following")
    public List<Follow> followingList( @RequestParam ("email") String email){
        return followService.showFollowerList(email);
    }

// 나를 팔로우한 사람 리스트
@GetMapping("/user/followed")
public List<Follow> followedList( @RequestParam ("email") String email){
    return followService.showFollowedList(email);
}*/
}
