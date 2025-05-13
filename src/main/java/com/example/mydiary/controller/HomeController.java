package com.example.mydiary.controller;

import java.net.http.HttpRequest;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.example.mydiary.entity.Member;

import jakarta.servlet.http.HttpSession;

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
    public String mainPage(Model model, HttpSession session) {
        Member user = (Member) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("loginProvider",
                    user.getProvider() != null ? user.getProvider() : "general");
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
