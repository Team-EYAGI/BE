//package com.example.eyagi.service;
//
//import com.example.eyagi.dto.FollowDto;
//import com.example.eyagi.repository.FollowRepository;
//import com.example.eyagi.security.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.qlrm.mapper.JpaResultMapper;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import javax.transaction.Transactional;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class FollowService {
//
//    private final FollowRepository followRepository;
//    private final EntityManager em;
//
//    @Transactional
//    public ResponseEntity<?> follow(UserDetailsImpl userDetails, Long toUserId) {
//        if(followRepository.findFollowByFromUserIdAndToUserId(userDetails.getUser().getId(), toUserId) != null) throw new IllegalArgumentException("이미 팔로우 하였습니다.");
//        followRepository.follow(userDetails.getUser().getId(), toUserId);
//        return new ResponseEntity<>("팔로우 성공", HttpStatus.OK);
//    }
//
//    @Transactional
//    public ResponseEntity<?> unFollow(UserDetailsImpl userDetails, Long toUserId) {
//        followRepository.unFollow(userDetails.getUser().getId(), toUserId);
//        return new ResponseEntity<>("언팔로우 성공", HttpStatus.OK);
//    }
//
//
////    public List<FollowDto> getFollower(long profileId, long loginId) {
////        StringBuffer sb = new StringBuffer();
////        sb.append("SELECT u.id, u.email, u.user_image, ");
////        sb.append("if ((SELECT 1 FROM follow WHERE from_user_id = ? AND to_user_id = u.id), TRUE, FALSE) AS followState, ");
////        sb.append("if ((?=u.id), TRUE, FALSE) AS loginUser ");
////        sb.append("FROM user u, follow f ");
////        sb.append("WHERE u.id = f.from_user_id AND f.to_user_id = ?");
////        // 쿼리 완성
////        Query query = em.createNativeQuery(sb.toString())
////                .setParameter(1, loginId)
////                .setParameter(2, loginId)
////                .setParameter(3, profileId);
////
////        //JPA 쿼리 매핑 - DTO에 매핑
////        JpaResultMapper result = new JpaResultMapper();
////        List<FollowDto> followDtoList = result.list(query, FollowDto.class);
////        return followDtoList;
////    }
////
////    @Transactional
////    public List<FollowDto> getFollowing(long profileId, long loginId) {
////
////
////        return followDtoList;
////    }
//}
