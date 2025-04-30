package com.example.mydiary.repository;

import java.util.Optional;

import com.example.mydiary.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
}
