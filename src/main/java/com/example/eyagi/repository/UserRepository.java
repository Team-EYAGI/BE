package com.example.eyagi.repository;


import com.example.eyagi.model.User;
import com.example.eyagi.model.UserRole;
import com.example.eyagi.repository.QRepository.UserCustomRepositiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    Optional<User> findByKakaoId(Long kakaoId);
    List<User>findByRole(UserRole role);


    @Query(value = "select seller.id as id, seller.userProfile.userImage as userImage, seller.username as username, seller.email as email from User as seller where seller.role =:role")
    Page<UserCustomRepositiry> findByOrderByRole(UserRole role, Pageable pageable);


}