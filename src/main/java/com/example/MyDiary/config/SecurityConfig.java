package com.example.mydiary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.mydiary.service.MyUserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyUserService myUserService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests(authz -> authz
                // 로그인 관련 경로 모두 허용
                .requestMatchers("/login", "/login/**").permitAll()
                .requestMatchers("/css/**", "/js/**","/error").permitAll()
                .anyRequest().authenticated())
            
            .formLogin(form -> form //ID / PW 로그인 사용중
                .loginPage("/login")
                .loginProcessingUrl("/authentication")
                .usernameParameter("usernameInput")
                .passwordParameter("passwordInput")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error"))

            .oauth2Login(oauth2 -> oauth2 //소셜 로그인도 설정됨
                .loginPage("/login")  // 소셜 로그인도 동일한 로그인 페이지 사용
                .defaultSuccessUrl("/", true)) // 성공 시 메인으로 이동

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }
}

