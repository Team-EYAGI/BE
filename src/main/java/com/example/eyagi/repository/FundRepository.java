package com.example.eyagi.repository;

import com.example.eyagi.model.Fund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FundRepository extends JpaRepository<Fund, Long> {
    List<Fund> findAllByOrderByCreatedAtDesc();
}
