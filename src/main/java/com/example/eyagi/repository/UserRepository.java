package com.example.eyagi.repository;


import com.example.eyagi.dto.KakaoUserInfoDto;
import com.example.eyagi.model.Follow;
import com.example.eyagi.model.User;
import com.example.eyagi.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    Optional<User> findByKakaoId(Long kakaoId);
    List<User>findByRole(UserRole role);



}