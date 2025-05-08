package com.example.mydiary.controller;

import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String mainPage() {
        return "main";
    }

    // 로그아웃 후 인트로 페이지로 리다이렉트
    @GetMapping("/intro")
    public String introPage() {
        return "intro";
    }
}
