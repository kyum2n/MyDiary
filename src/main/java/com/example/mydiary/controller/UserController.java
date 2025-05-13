package com.example.mydiary.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.UserMapper;
import com.example.mydiary.service.UserService;
import com.example.mydiary.service.WeatherService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final WeatherService weatherService;

    @Value("${upload.profile-dir}")
    private String uploadDir;

    // 사용자 프로필
    @GetMapping("/myProfile")
    public String showProfile(Model model, HttpSession session) {
        // 세션에서 로그인 정보 확인
        Member user = (Member) session.getAttribute("user");

        if (user == null)
            return "redirect:/login";

        user.setUPwd("********");

        String weatherNow = weatherService.getWeather("Seoul");
        model.addAttribute("weatherNow", weatherNow);

        return "myProfile";
    }

    // 프로필 이미지 변경
    @PostMapping("/myProfile/uploadUImage")
    public String updateUImage(@RequestParam("uImage") MultipartFile uImage, HttpSession session) {
        Member user = (Member) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        userService.updateUImage(user.getUId(), uImage);
        return "redirect:/myProfile";
    }

    // 이미지 요청 핸들링 (프로필 사진 표시용)
    @GetMapping("/uploads/profile/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveProfileImage(@PathVariable String filename) {
        try {
            Path realUploadDir = Paths.get(uploadDir).toAbsolutePath();
            Path filePath = realUploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 비밀번호 변경 페이지
    @GetMapping("/changePwd")
    public String showChangePasswordPage(HttpSession session, Model model) {
        Member user = (Member) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("timestamp", System.currentTimeMillis());

        return "changePwd";
    }

    // 비밀번호 변경 처리
    @PostMapping("/changePwd")
    public String changePassword(@RequestParam String currentPwd,
            @RequestParam String newPwd,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Member loginUser = (Member) session.getAttribute("user");

        if (loginUser == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/intro";
        }

        if (loginUser.getProvider() != null) {
            model.addAttribute("error", "소셜 로그인 사용자는 비밀번호 변경이 불가능합니다.");
            return "changePwd";
        }

        Member user = userMapper.selectUserByUId(loginUser.getUId());

        if (!passwordEncoder.matches(currentPwd, user.getUPwd())) {
            model.addAttribute("error", "기존 비밀번호가 일치하지 않습니다.");
            return "changePwd";
        }

        if (passwordEncoder.matches(newPwd, user.getUPwd())) {
            model.addAttribute("error", "새 비밀번호는 기존과 달라야 합니다.");
            return "changePwd";
        }

        String encodedNewPwd = passwordEncoder.encode(newPwd);
        userMapper.updatePassword(user.getUId(), encodedNewPwd);

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("message", "비밀번호가 변경되었습니다.");
        return "redirect:/login";
    }
};