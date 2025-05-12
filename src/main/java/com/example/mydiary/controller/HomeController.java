package com.example.mydiary.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.mydiary.entity.Member;
import com.example.mydiary.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {

    private final UserService userService;

    // 로그인 전 인트로 페이지
    @GetMapping("/")
    public String toHome() {
        return "intro";
    }

    // 로그인 성공 시 메인 페이지
    @GetMapping("/main")
    public String mainPage(Model model, Principal principal) {
        if (principal != null) {
            Object principalObj = ((Authentication) principal).getPrincipal();

            if (principalObj instanceof Member loginUser) {
                model.addAttribute("user", loginUser);
                model.addAttribute("loginProvider",
                        loginUser.getProvider() != null ? loginUser.getProvider() : "general");
            } else if (principalObj instanceof DefaultOAuth2User oauthUser) {
                // (1차 수정)카카오 로그인시 메인화면가는 부분에서 에러나는 문제 발생
                String uId = (String) oauthUser.getAttribute("id");
                // // (2차 수정) 디버그 + 예외 회피 + 안정적인 키 처리
                // // String uId = String.valueOf(oauthUser.getAttribute("id"));
                // Object rawId = oauthUser.getAttribute("id");
                // String uId = rawId != null ? String.valueOf(rawId) : null;

                // if (uId == null || uId.isBlank()) {
                //     System.out.println("소셜 로그인 사용자 ID가 null이거나 잘못된 값입니다.");
                //     return "redirect:/error"; 
                // }

                Member user = userService.findUserByUId(uId);
                if (user == null) {
                    System.out.println("해당 uId로 사용자 정보가 없습니다: " + uId);
                    return "redirect:/error";
                }
                // //여기까지 추가 내용

                model.addAttribute("user", user);
                model.addAttribute("loginProvider", oauthUser.getAttribute("provider") != null
                        ? oauthUser.getAttribute("provider")
                        : "social");
            } else {
                model.addAttribute("loginProvider", "unknown");
            }
        } else {
            model.addAttribute("loginProvider", "general");
        }

        return "main";
    }

    // 로그아웃 후 인트로 페이지로 리다이렉트
    @GetMapping("/intro")
    public String introPage() {
        return "intro";
    }
}
