package com.example.mydiary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uId;

    @Column(nullable = false, unique = true)
    private String uEmail;

    @Column(nullable = false)
    private String uPwd;

    private String uName;
    private String uImage;

    private String provider;      // ex: "naver", "google"
    private String providerId;    // 네이버에서 온 고유 ID
}