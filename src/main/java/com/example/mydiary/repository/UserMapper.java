package com.example.mydiary.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mydiary.entity.Member;

@Mapper
public interface UserMapper {
    // 사용자 id로 사용자 정보 조회
    Member selectUserByUId(String uId);

    // 이메일로 사용자 정보 조회
    Member findByEmail(@Param("email") String email);

    // 사용자 프로필 사진 변경
    void updateUImage(String uId, String uImage);

    // 비밀번호 변경
    void updatePassword(@Param("uId") String uId, @Param("uPwd") String uPwd);
}