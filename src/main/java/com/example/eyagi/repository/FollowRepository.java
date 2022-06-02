package com.example.eyagi.repository;

import com.example.eyagi.model.Follow;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {

    @EntityGraph(attributePaths = {"follower","followed"}, type = EntityGraph.EntityGraphType.FETCH)
//    @Query("select distinct u from Follow u left join u.follower")
    List<Follow> findByFollower_Id(Long id); //팔로우를 신청한 사람 .일반 사용자

    List<Follow> findByFollowed_Id(Long id); //팔로우를 신청 받은 사람 . 크리에이터

    Optional<Follow> findByFollower_IdAndFollowed_Id (Long followerId , Long followedId);

}
