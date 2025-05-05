package com.example.mydiary.repository;

import com.example.mydiary.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Member 엔티티에 대한 DB 접근 인터페이스
public interface MemberRepository extends JpaRepository<Member, String> {
    
    // uId(회원 ID)로 회원 검색
    Optional<Member> findByuId(String uId);
}