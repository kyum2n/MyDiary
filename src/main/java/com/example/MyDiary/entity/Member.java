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
    private Long uId;

    @Column(nullable = false, unique = true)
    private String uEmail;

    @Column(nullable = false)
    private String uPwd;

    private String uName;
    private String uImage;

    @Transient
    private String provider;

    @Transient
    private String providerId;
}