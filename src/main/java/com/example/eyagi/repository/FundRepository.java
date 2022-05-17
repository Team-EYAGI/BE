package com.example.eyagi.repository;

import com.example.eyagi.model.Books;
import com.example.eyagi.model.Fund;
import com.example.eyagi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FundRepository extends JpaRepository<Fund, Long> {
    List<Fund> findAllByUserIdOrderByFundIdDesc(Long user_id);
    List<Fund> findAllByOrderByFundIdDesc();
    Optional<Fund> findByUserAndBooks(User seller, Books books);

}
