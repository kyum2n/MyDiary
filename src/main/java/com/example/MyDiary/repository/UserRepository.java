package com.example.mydiary.repository;

import com.example.mydiary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByDisplayName(String displayName); // 소셜 사용자 매핑용
}