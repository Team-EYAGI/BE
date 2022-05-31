package com.example.eyagi.repository;

import com.example.eyagi.model.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowQRepository {
        private final EntityManager em;

//    @EntityGraph(attributePaths = {"follower, followed"}, type = EntityGraph.EntityGraphType.FETCH)
    public List<Follow> findFollowingListById(Long userId) {
        List<Follow> followLists = em.createQuery("select fl from Follow fl " +
                        "join fetch fl.follower flf " +
                        "join fetch flf.userProfile uP " +
                        "where flf.id=:userId", Follow.class)
                .setParameter("userId", userId)
                .getResultList();
        return followLists;
    }

    public List<Follow> findFollowedListById(Long userId) {
        List<Follow> followedLists = em.createQuery("select fl from Follow fl " +
                        "join fetch fl.followed fld " +
                        "join fetch fld.userProfile uP " +
                        "where fld.id=:userId", Follow.class)
                .setParameter("userId", userId)
                .getResultList();
        return followedLists;
    }
}
