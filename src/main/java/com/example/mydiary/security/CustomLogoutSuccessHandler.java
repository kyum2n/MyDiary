package com.example.mydiary.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import java.io.IOException;

/*로그아웃 성공 시 실행되는 커스텀 핸들러
 * - 소셜 = 카카오 로그아웃 URL -> 리다이렉트 / 네이버는 main에서 처리중
 * = 로그아웃 방식이 다르기에 처리방식도 다름
 * 클라이언트 팝업 필수임 = 네이버
 * 서버 리다이렉트 가능함 = 카카오
 * 
 * - 일반 = /intro로 리다이렉트
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        request.getSession().invalidate();

        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {

            String kakaoClientId = "b881c38fcf1147a2d3c238e784c3be5b";
            String kakaoRedirectUri = "http://localhost:8080/logout-kakao";
            String kakaoLogoutUrl = "https://kauth.kakao.com/oauth/logout?client_id="
                    + kakaoClientId + "&logout_redirect_uri=" + kakaoRedirectUri;

            response.sendRedirect(kakaoLogoutUrl);
            return;
        }

        response.sendRedirect("/intro");
    }
}
