package com.example.mydiary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 루트 진입 시
    @GetMapping("/")
    public String intro() {
        return "intro"; // templates/intro.html
    }

    // 로그아웃 후 리다이렉트되는 /intro 처리
    @GetMapping("/intro")
    public String introPage() {
        return "intro"; // templates/intro.html
    }
}