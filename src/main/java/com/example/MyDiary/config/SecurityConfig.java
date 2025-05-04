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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(authz -> authz
            .requestMatchers("/", "/intro", "/login", "/login/**", "/logout-success").permitAll()
            .requestMatchers("/css/**", "/js/**", "/img/**", "/error").permitAll()
            .anyRequest().authenticated()
        )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/authentication")
                .usernameParameter("usernameInput")
                .passwordParameter("passwordInput")
                .defaultSuccessUrl("/main", true)
                .failureUrl("/login?error")
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService))
                .defaultSuccessUrl("/main", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}