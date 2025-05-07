package com.example.mydiary.controller;

import java.security.Principal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.UserMapper;
import com.example.mydiary.service.UserService;
import com.example.mydiary.service.WeatherService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final WeatherService weatherService;

    // 사용자 프로필
    @GetMapping("/myProfile")
    public String showProfile(Model model, Principal principal) {

        if (principal == null) {
            System.out.println("Principal is null!");
            return "redirect:/login";
        }

        String uId = principal.getName();
        Member user = userService.findUserByUId(uId);
        if (user != null) {
            user.setUPwd("********");
            if (user.getUImage() == null || user.getUImage().isEmpty()) {
                user.setUImage("/image/defaultProfileImage.webp");
            }
            model.addAttribute("user", user);
        }

        // 날씨 정보 가져오기
        String weatherNow = weatherService.getWeather("Seoul");
        model.addAttribute("weatherNow", weatherNow);

        return "myProfile";
    }

    // 프로필 사진 변경
    @PostMapping("/myProfile/uploadUImage")
    public String updateUImage(@RequestParam("uImage") MultipartFile uImage, Principal principal) {
        String uId = principal.getName();
        userService.updateUImage(uId, uImage);
        return "redirect:/myProfile";
    }

    // 비밀번호 변경 페이지로 이동
    @GetMapping("/changePwd")
    public String showChangePasswordPage() {
        return "changePwd";
    }

    // 비밀번호 변경 처리
    @PostMapping("/changePwd")
    public String changePassword(@RequestParam String currentPwd,
            @RequestParam String newPwd,
            HttpSession session,
            Model model) {

        Member loginUser = (Member) session.getAttribute("loginUser");

        if (loginUser == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/intro";
        }

        Member user = userMapper.selectUserByUId(loginUser.getUId());

        // 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(currentPwd, user.getUPwd())) {
            model.addAttribute("error", "기존 비밀번호가 일치하지 않습니다.");
            return "changePwd";
        }

        // 새 비밀번호 암호화 방식으로 비교
        if (passwordEncoder.matches(newPwd, user.getUPwd())) {
            model.addAttribute("error", "새 비밀번호는 기존과 달라야 합니다.");
            return "changePwd";
        }

        // 새 비밀번호 암호화 후 저장
        String encodedNewPwd = passwordEncoder.encode(newPwd);
        userMapper.updatePassword(user.getUId(), encodedNewPwd);

        model.addAttribute("message", "비밀번호가 변경되었습니다.");
        return "redirect:/intro";
    }

}
