package com.example.eyagi.service;


import com.example.eyagi.model.Follow;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.FollowRepository;
import com.example.eyagi.repository.UserRepository;
import com.example.eyagi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
   private final FollowRepository followRepository;
   private final UserRepository userRepository;

/*
// 팔로우 목록 가져오기
    public List<Follow> showFollowerList(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        userRepository.findByFollowingList(user.getEmail());
        return userRepository.findByFollowingList(user.getEmail());
    }
    public List<Follow>showFollowedList(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        return userRepository.findByFollowedList(user.getEmail());
    }
*/

//팔로우 하기
    public boolean startFollow(UserDetailsImpl userDetails, String sellerEmail){
        //유저정보찾기
        String username = userDetails.getUsername();
        User user = userRepository.findByEmail(username).orElse(null);
        User seller = userRepository.findByEmail(sellerEmail).orElse(null);

        //자기자신 팔로우 금지
        if(user == seller){ return false;}


        assert user != null;
        List<Follow>follower = seller.getFollowedList();

        for (Follow f : follower){
            if(f.getFollower() == user){
                followRepository.deleteById(f.getId());
                return false ;
            }
        }

        Follow following = Follow.builder()
                .followed(seller)
                .follower(user)
                .build();


        followRepository.save(following);

        return true;
    }



}
