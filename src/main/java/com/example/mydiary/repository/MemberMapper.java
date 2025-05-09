package com.example.mydiary.repository;

import com.example.mydiary.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    // 일반 로그인 시 사용자 id로 사용자 정보 조회
    Member findByuId(String uId);

    // 소셜 로그인 시 사용자 email로 사용자 정보 조회
    Member findByEmail(String uEmail);

    // 회원가입 시 사용자 정보 저장
    void insertMember(Member member);

    // 사용자 조회
    String findUIdByEmail(String email);

    Member selectUserByUId(String uId);

    void updatePassword(@Param("uId") String uId, @Param("uPwd") String uPwd);
}