package com.example.mydiary.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private String uId;
    private String uEmail;
    private String uName;
    private String uPwd;
    private String uImage;
    private String provider;
    private String providerId;
}