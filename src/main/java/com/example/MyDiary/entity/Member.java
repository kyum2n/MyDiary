package com.example.mydiary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name = "uid")
    private String uId;

    @Column(name = "uemail", nullable = false, unique = true)
    private String uEmail;
    
    @Column(name = "uname")
    private String uName;
    
    @Column(name = "upwd")
    private String uPwd;
    
    @Column(name = "uimage")
    private String uImage;

    // ✅ 소셜 로그인용 필드 추가
    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;
}