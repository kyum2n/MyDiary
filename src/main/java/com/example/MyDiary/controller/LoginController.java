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
        // 로그인 실패 메시지
        if (request.getParameter("error") != null) {
            model.addAttribute("error", "로그인에 실패했습니다. 아이디나 비밀번호를 확인하세요.");
        }

        // 로그아웃 성공 메시지
        if (request.getParameter("logout") != null) {
            model.addAttribute("message", "성공적으로 로그아웃되었습니다.");
        }

        return "login"; // → templates/login.html
    }

    // (선택) 메인 페이지 매핑
    @GetMapping("/")
    public String index() {
        return "index"; // → templates/index.html
    }

    // 네이버 콜백 후 성공 시 리디렉션 처리 (Spring Security가 자동 처리하지만 예시로 둠)
    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "redirect:/";  // 로그인 후 홈으로 이동
    }
}
