package com.example.mydiary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // 로그인 성공 시 메인 페이지 진입
    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }
}