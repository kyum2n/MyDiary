package com.example.mydiary.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        if (request.getParameter("error") != null) {
            model.addAttribute("error", "로그인에 실패했습니다. 아이디나 비밀번호를 확인하세요.");
        }

        if (request.getParameter("logout") != null) {
            model.addAttribute("message", "성공적으로 로그아웃되었습니다.");
        }

        return "login"; // login.html
    }

    // 로그아웃 성공 페이지
    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "logout-success"; // logout-success.html
    }
}