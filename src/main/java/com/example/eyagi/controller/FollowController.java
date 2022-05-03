//package com.example.eyagi.controller;
//
//import com.example.eyagi.security.UserDetailsImpl;
//import com.example.eyagi.service.FollowService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RequiredArgsConstructor
//@RestController
//public class FollowController {
//
//    private final FollowService followService;
//
//    @PostMapping("/follow/{toUserId}")
//    public ResponseEntity<?> followUser(@PathVariable Long toUserId,
//                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return followService.follow(userDetails, toUserId);
//    }
//
//    @DeleteMapping("/follow/{toUserId}")
//    public ResponseEntity<?> unFollowUser(@PathVariable Long toUserId,
//                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return followService.unFollow(userDetails, toUserId);
//    }
//
////    @GetMapping("/follow/{profileId}/follower")
////    public ResponseEntity<?> getFollower(@PathVariable Long profileId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
////        return new ResponseEntity<>(followService.getFollower(profileId, userDetails.getUser().getId()), HttpStatus.OK);
////    }
////
////    @GetMapping("/follow/{profileId}/following")
////    public ResponseEntity<?> getFollowing(@PathVariable Long profileId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
////        return new ResponseEntity<>(followService.getFollowing(profileId, userDetails.getUser().getId()), HttpStatus.OK);
////    }
//}
