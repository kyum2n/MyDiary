package com.example.mydiary.repository;

import com.example.mydiary.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByuEmail(String uEmail);
}