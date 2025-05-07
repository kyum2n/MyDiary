package com.example.mydiary.controller;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.MemberMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        if (request.getParameter("error") != null) {
            model.addAttribute("error", "로그인 실패: 아이디/비밀번호 확인");
        }

        if (request.getParameter("logout") != null) {
            model.addAttribute("message", "로그아웃 완료");
        }

        return "login";
    }

    // 로그아웃 성공 시 인트로 페이지로 이동
    @GetMapping("/logout")
    public String logoutSuccess() {
        return "redirect:/intro";
    }

    // 카카오 로그아웃
    // @GetMapping("/logout-kakao")
    // public String kakaoLogoutRedirect() {
    // return "redirect:/intro";
    // }

    // 회원가입
    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@ModelAttribute Member member) {
        member.setUPwd(passwordEncoder.encode(member.getUPwd()));
        memberMapper.insertMember(member);
        return "redirect:/login";
    }

}