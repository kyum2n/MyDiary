package com.example.mydiary.repository;

import com.example.mydiary.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    Member findByUid(String uId);
    Member findByEmail(String uEmail);
    void insertMember(Member member);

    // ✅ XML과 매칭되도록 추가
    void updatePassword(Member member);
}