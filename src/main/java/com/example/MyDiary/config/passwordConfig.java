package com.example.mydiary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 설정 클래스임을 명시
public class PasswordConfig {

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
     * - BCrypt 알고리즘 사용 (보안 강도 높음)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}