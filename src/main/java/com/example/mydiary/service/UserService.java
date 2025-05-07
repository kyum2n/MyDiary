package com.example.mydiary.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.mydiary.entity.Member;

public interface UserService {
    // 사용자 id로 사용자 정보 조회
    Member findUserByUId(String uId);

    // 사용자 프로필 사진 변경
    void updateUImage(String uId, MultipartFile uImage);
}
