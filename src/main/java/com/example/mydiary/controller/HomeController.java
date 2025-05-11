package com.example.mydiary.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.example.mydiary.entity.Member;

@Controller
@AllArgsConstructor
public class HomeController {

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
                model.addAttribute("user", null);
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
