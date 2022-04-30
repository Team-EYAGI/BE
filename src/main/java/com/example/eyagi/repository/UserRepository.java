package com.example.eyagi.repository;


import com.example.eyagi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User findFollowUserByEmail(String email);
    List<User> findAll();
    Optional<User> findUserById(Long userId);
}