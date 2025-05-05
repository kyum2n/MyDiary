package com.example.mydiary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // 사용자(Member) 테이블과 매핑
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id // 기본키
    @Column(name = "uid")
    private String uId;

    @Column(name = "uemail", nullable = false, unique = true)
    private String uEmail; // 이메일
    
    @Column(name = "uname")
    private String uName; // 사용자 이름
    
    @Column(name = "upwd")
    private String uPwd; // 비밀번호
    
    @Column(name = "uimage")
    private String uImage; // 프로필 이미지 파일명

    // 소셜 로그인용 필드 추가
    @Column(name = "provider")
    private String provider; // 소셜 로그인 제공자

    @Column(name = "provider_id")
    private String providerId; // 소셜 로그인 식별 ID
}