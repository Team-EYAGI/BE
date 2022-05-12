package com.example.eyagi.repository;

import com.example.eyagi.model.Fund;
import com.example.eyagi.model.FundHeart;
import com.example.eyagi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FundHeartRepository extends JpaRepository<FundHeart, Long> {
    boolean existsByUserAndFund(User user, Fund foundFund);
    void deleteByUserAndFund(User user, Fund foundFund);
}
