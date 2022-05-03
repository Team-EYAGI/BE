package com.example.eyagi.repository;

import com.example.eyagi.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findFollowByFromUserIdAndToUserId(Long from_user_id, Long to_user_id);

    @Query(value = "SELECT count(*) FROM follow WHERE toUserId = ?1", nativeQuery = true)
    int mCountByFollower(int toUserId);

    @Query(value = "SELECT count(*) FROM follow WHERE fromUserId = ?1", nativeQuery = true)
    int mCountByFollowing(int fromUserId);

    @Query(value = "SELECT count(*) FROM follow WHERE fromUserId = ?1 AND toUserId = ?2", nativeQuery = true)
    int mFollowState(int loginUserId, int pageUserId);

    // 수정,삭제,추가시에는 모디파이 어노테이션 필요 @Modifying
    @Modifying
    @Query(value = "INSERT INTO follow(from_user_id, to_user_id) VALUES(:fromId, :toId)", nativeQuery = true)
    void follow(Long fromId, Long toId);

    @Modifying
    @Query(value = "DELETE FROM follow WHERE from_user_id = :fromId AND to_user_id = :toId", nativeQuery = true)
    void unFollow(Long fromId, Long toId);
}
