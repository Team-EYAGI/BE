package com.example.eyagi.repository;

import com.example.eyagi.model.Books;
import com.example.eyagi.model.Fund;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.QRepository.FundCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface FundRepository extends JpaRepository<Fund, Long> {

    @EntityGraph(attributePaths = {"books","user"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Fund> findByFundId(Long id);

    @EntityGraph(attributePaths = {"books","user"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Fund> findAllByUserIdOrderByFundIdDesc(Long user_id);

    @EntityGraph(attributePaths = {"books","user"},type = EntityGraph.EntityGraphType.FETCH)
    @Query("select distinct u from Fund u left join u.user")//distinct로 중복 제거
    List<Fund> findAllByOrderByFundIdDesc();

    Optional<Fund> findByUserAndBooks(User seller, Books books);

    @Query(value = "select f.fundId as fundId, f.user.username as sellerName, f.heartCnt as likeCnt, " +
            "f.audioFund.fundFile as fundFile, f.books.title as bookTitle, f.books.bookImg as bookImg, " +
            "f.fundingGoals as fundingGoals, f.successGoals as successFunding from Fund f")
    Page<FundCustomRepository> findByOrderByFundId(Pageable pageable);
}
