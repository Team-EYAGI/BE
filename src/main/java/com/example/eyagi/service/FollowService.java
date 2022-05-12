package com.example.eyagi.service;

import com.example.eyagi.dto.FollowDto;
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

 // email로 user찾기
public User findUserName(String username){
    return userRepository.findByUsername(username).orElse(null);
}

    public boolean startFollow(UserDetailsImpl userDetails, String sellername){
        //유저정보찾기
        String username = userDetails.getUsername();
        User user = userRepository.findByEmail(username).orElse(null);
        User seller = findUserName(sellername);

        //자기자신 팔로우 금지
        if(user == seller){ return false;}


        assert seller != null;
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

    //나를 팔로우한 사람 리스트
    public  List<FollowDto>getFollowerList(String username) {

        User seller = findUserName(username);

        List<Follow> follower = seller.getFollowedList();

        List<FollowDto> followerList = new ArrayList<>();

        for (Follow f : follower) {
            try {
                FollowDto followDto = FollowDto.builder()
                        .img(f.getFollower().getUserProfile().getUserImage())
                        .name(f.getFollower().getUsername())
                        .build();
                followerList.add(followDto);

            } catch (NullPointerException e) {
                FollowDto followDto = FollowDto.builder()
                        .name(f.getFollower().getUsername())
                        .build();
                followerList.add(followDto);
            }

        }
        seller.setFollwerCnt(followerList.size());

        return followerList;

    }
    //내가 팔로잉한 사람 리스트
   public List<FollowDto>getFollowingList(String username){
     User user = findUserName(username);
     List<Follow>following = user.getFollowingList();

     List<FollowDto>followingList = new ArrayList<>();

     for(Follow f: following){
         try{
                FollowDto followDto = FollowDto.builder()
                        .name(f.getFollowed().getUsername())
                        .img(f.getFollowed().getUserProfile().getUserImage())
                        .build();
                followingList.add(followDto);
         }catch (NullPointerException e){
             FollowDto followDto = FollowDto.builder()
                     .name(f.getFollowed().getUsername())
                     .build();
             followingList.add(followDto);
         }
     }
     user.setFollowingCnt(followingList.size());

     return followingList;
   }


    }






