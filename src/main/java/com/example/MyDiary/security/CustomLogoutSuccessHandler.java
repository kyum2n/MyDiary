package com.example.mydiary.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
        if (authentication == null) {
            response.sendRedirect("/login?logout");
            return;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User) {
            response.sendRedirect("/logout-success");
        } else {
            response.sendRedirect("/login?logout");
        }
    }
}