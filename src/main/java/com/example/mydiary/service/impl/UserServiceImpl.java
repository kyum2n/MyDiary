package com.example.mydiary.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.UserMapper;
import com.example.mydiary.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Value("${upload.profile-dir}")
    private String uploadDir;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 사용자 id로 사용자 정보 조회
    @Override
    public Member findUserByUId(String uId) {
        return userMapper.selectUserByUId(uId);
    }

    // 이메일로 사용자 정보 조회
    @Override
    public Member findUserByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    // 사용자 프로필 사진 변경
    @Override
    public void updateUImage(String uId, MultipartFile uImage) {
        if (uImage == null || uImage.isEmpty()) {
            System.out.println("+++++++++++프로필 이미지가 없습니다.");
            return;
        }

        try {
            // 확장자 검증
            String originalFilename = uImage.getOriginalFilename();
            String extension = getFileExtension(originalFilename).toLowerCase();
            String fileName = UUID.randomUUID() + extension;

            // 파일 경로(각자의 로컬 컴퓨터에 따라 절대 경로 설정)
            Path realUploadDir = Paths.get(uploadDir).toAbsolutePath();
            Files.createDirectories(realUploadDir);

            Path filePath = realUploadDir.resolve(fileName);
            Files.write(filePath, uImage.getBytes());

            // 디렉토리 없으면 생성
            Files.createDirectories(filePath.getParent());

            // 파일 저장
            Files.write(filePath, uImage.getBytes());

            // DB 저장 경로 (웹 경로 기준)
            String dbPath = "/uploads/profile/" + fileName;

            System.out.println("업데이트할 이미지 경로: " + dbPath);
            System.out.println("업데이트할 사용자 ID: " + uId);

            // DB 업데이트
            userMapper.updateUImage(uId, dbPath);

        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 저장 실패", e);
        }
    }

    // 파일 확장자 추출
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
