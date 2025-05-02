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
    @Column(name = "uid")
    private String uId; // 수동 지정, @GeneratedValue 제거

    @Column(name = "uemail", nullable = false, unique = true)
    private String uEmail;

    @Column(name = "upwd", nullable = false)
    private String uPwd;

    @Column(name = "uname")
    private String uName;

    @Column(name = "uimage")
    private String uImage;

    @Transient
    private String provider;

    @Transient
    private String providerId;
}