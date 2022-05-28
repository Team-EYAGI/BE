package com.example.eyagi.repository;

import com.example.eyagi.model.AllChatInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllChatInfoRepository extends JpaRepository<AllChatInfo,Long> {
    List<AllChatInfo> findAllByUserId(Long userId);
}
