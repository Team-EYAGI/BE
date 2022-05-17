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
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    //
//public User findUserName(String username){
//    return userRepository.findByUsername(username).orElse(null);
//}

    public Map<String, Object> startFollow(User user, Long id) {
        //유저정보찾기

        User seller = userService.findUserId(id);
        Map<String, Object> followONOff = new HashMap<>();
        //자기자신 팔로우 금지
        if (user.getId().equals(seller.getId())) {
            followONOff.put("followStatus", false);
            return followONOff;
        }

//        assert user != null;
//        assert seller != null;
        List<Follow> follower = seller.getFollowedList();
        for (Follow f : follower) {
            if (f.getFollower().getId().equals(user.getId())) {
                followRepository.deleteById(f.getId());
                seller.sumFollwerCnt(-1);
                user.sumFollowingCnt(-1);

                followONOff.put("followStatus", false);
                followONOff.put("followCount", seller.getFollwerCnt());
                return followONOff;
            }
        }

        Follow following = Follow.builder()
                .followed(seller)
                .follower(user)
                .build();

        followRepository.save(following);
        seller.sumFollwerCnt(1);
        user.sumFollowingCnt(1);
        userRepository.save(user);
        followONOff.put("followStatus", true);
        followONOff.put("followCount", seller.getFollwerCnt());
        return followONOff;
    }

    //나를 팔로우한 사람 리스트
    public List<FollowDto> getFollowerList(Long id) { //pk 값으로 조회하는 걸로 바꾸는 것이 낫지 않을까?

        User seller = userService.findUserId(id);

        List<Follow> follower = seller.getFollowedList();

        List<FollowDto> followerList = new ArrayList<>();

        for (Follow f : follower) {
//            try {
//                FollowDto followDto = FollowDto.builder()
//                        .img(f.getFollower().getUserProfile().getUserImage())
//                        .name(f.getFollower().getUsername())
//                        .build();
//                followerList.add(followDto);
//
//            } catch (NullPointerException e) {
//                FollowDto followDto = FollowDto.builder()
//                        .name(f.getFollower().getUsername())
//                        .build();
//                followerList.add(followDto);
//            }
            FollowDto dto = new FollowDto();
            dto.following(f);
            followerList.add(dto);
        }

        return followerList;

    }

    //내가 팔로잉한 사람 리스트
    public List<FollowDto> getFollowingList(Long id) {
        User user = userService.findUserId(id);
        List<Follow> following = user.getFollowingList();

        List<FollowDto> followingList = new ArrayList<>();

        for (Follow f : following) {
//         try{
//             FollowDto followDto = FollowDto.builder()
//                        .name(f.getFollowed().getUsername())
//                        .img(f.getFollowed().getUserProfile().getUserImage())
//                        .build();
//                followingList.add(followDto);
//         }catch (NullPointerException e){
//             FollowDto followDto = FollowDto.builder()
//                     .name(f.getFollowed().getUsername())
//                     .build();
//             followingList.add(followDto);
//         }
            FollowDto dto = new FollowDto();
            dto.follower(f);
            followingList.add(dto);
        }

        return followingList;
    }

    public boolean followStatus( Long userId, Long sellerId){
        Optional<Follow> followCheck = followRepository.findByFollower_IdAndFollowed_Id(userId, sellerId);
        if (followCheck.isPresent()){
            return true; //null이 아니면 트루
        } else {
            return false; //null이면 펄스
        }
    }


}






