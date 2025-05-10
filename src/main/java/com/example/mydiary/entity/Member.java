package com.example.mydiary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private String uId;
    private String uName;
    private String uPwd;
    private String uEmail;
    private String uImage;
    private String provider;
    private String providerId;
}