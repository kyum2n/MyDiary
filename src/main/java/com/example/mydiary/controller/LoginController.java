package com.example.mydiary.controller;

import java.util.Collections;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.MemberMapper;
import com.example.mydiary.service.MailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

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

        System.out.println("[DEBUG] 조회된 사용자 이메일: " + user.getUEmail());
        System.out.println("[DEBUG] 임시 비밀번호: " + tempPwd);

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
            model.addAttribute("error", "Login failed: Invalid ID or password");
        }

        if (request.getParameter("logout") != null) {
            model.addAttribute("message", "Logout successful");
        }

        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String uId,
            @RequestParam String uPwd,
            HttpServletRequest request,
            HttpSession session,
            Model model) {

        Member member = memberMapper.findByuId(uId);
        if (member == null || !passwordEncoder.matches(uPwd, member.getUPwd())) {
            model.addAttribute("error", "Invalid ID or password");
            return "login";
        }

        session.setAttribute("user", member);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                member, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        request.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", context);

        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String customLogoutRedirect(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return "redirect:/kakao-logout";
    }

    @GetMapping("/kakao-logout")
    public String redirectToKakaoLogout() {
        String kakaoLogoutUrl = "https://kauth.kakao.com/oauth/logout" +
                "?client_id=b881c38fcf1147a2d3c238e784c3be5b" +
                "&logout_redirect_uri=http://localhost:8080/logout-kakao";
        return "redirect:" + kakaoLogoutUrl;
    }

    @GetMapping("/logout-success")
    public String kakaoFinalLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/intro";
    }

    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        session.invalidate();
        return "redirect:/kakao-logout";
    }

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