package com.example.mydiary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.example.mydiary.security.CustomLogoutSuccessHandler;
import com.example.mydiary.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration // 시큐리티 설정 클래스
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    // 사용자 정보 및 인증에 필요한 Bean 주입
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 토큰을 쿠키로 저장 (자바스크립트에서 접근 가능)
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .userDetailsService(userDetailsService)

            // URL 접근 권한 설정
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/intro", "/login", "/login/**", "/logout-success").permitAll() // 공개 경로
                .requestMatchers("/css/**", "/js/**", "/img/**", "/error").permitAll() // 정적 리소스 허용
                .anyRequest().authenticated() // 나머지 경로는 로그인 필요
            )

            // 일반 로그인 설정
            .formLogin(form -> form
                .loginPage("/login") // 커스텀 로그인 페이지
                .loginProcessingUrl("/authentication") // 로그인 처리 URL
                .usernameParameter("usernameInput") // 아이디 입력 필드의 name
                .passwordParameter("passwordInput") // 비밀번호 입력 필드의 name
                .defaultSuccessUrl("/main", true) // 로그인 성공 시 이동
                .failureUrl("/login?error") // 실패 시 이동
            )

            // 소셜 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)) // 사용자 정보 처리 서비스
                .defaultSuccessUrl("/main", true)
            )

            // 로그아웃 처리
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler) // 성공 시 핸들러
                .invalidateHttpSession(true) // 세션 무효화
                .clearAuthentication(true) // 인증 정보 삭제
                .deleteCookies("JSESSIONID") // 쿠키 삭제
            );

        return http.build();
    }
}