package com.example.mydiary.controller;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.MemberMapper;
import com.example.mydiary.service.MailService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    // 아이디 찾기
    @PostMapping("/findId")
    @ResponseBody
    public String findIdByEmail(@RequestParam String email) {
        String uId = memberMapper.findUIdByEmail(email);
        if (uId != null) {
            String masked = uId.length() > 3 ? uId.substring(0, 3) + "***" : "***";
            return "회원님의 아이디는: " + masked;
        }
        return "해당 이메일로 등록된 아이디가 없습니다.";
    }

    // 메일 전송(임시 비밀번호 전송)
    @PostMapping("/findPwd")
    @ResponseBody
    public String findPwd(@RequestParam String uId) {
        Member user = memberMapper.selectUserByUId(uId);
        if (user == null) {
            return "존재하지 않는 아이디입니다.";
        }

        String tempPwd = UUID.randomUUID().toString().substring(0, 8);
        String encodedPwd = passwordEncoder.encode(tempPwd);

        System.out.println("🔍 [DEBUG] 조회된 사용자 이메일: " + user.getUEmail());
        System.out.println("🔐 [DEBUG] 임시 비밀번호: " + tempPwd);

        memberMapper.updatePassword(uId, encodedPwd);

        try {
            mailService.sendMail(user.getUEmail(), "임시 비밀번호", "임시 비밀번호: " + tempPwd);
            return "가입하신 이메일로 임시 비밀번호가 발송되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "메일 발송에 실패했습니다. 다시 시도해주세요.";
        }
    }

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