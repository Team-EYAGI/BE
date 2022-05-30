package com.example.eyagi.repository;

import com.example.eyagi.model.Fund;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FundQRepository {
    private final EntityManager em;
//    private final JPAQueryFactory queryFactory;

    @EntityGraph(attributePaths = {"audioBook"}, type = EntityGraph.EntityGraphType.FETCH)
    public Optional<Fund> findNewandById(Long fundId) {
        Fund fund = em.createQuery("select f " +
                        "from Fund f " +
                        "join fetch f.audioFund aF " +
                        "join fetch f.user u " +
                        "join fetch u.userProfile uf " +
                        "join fetch u.userLibrary uL " +
                        "join fetch f.books b " +
                        "where f.fundId = :fundId", Fund.class)
                .setParameter("fundId", fundId)
                .getSingleResult();
        return Optional.ofNullable(fund);
    }
}
