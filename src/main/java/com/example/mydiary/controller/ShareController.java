package com.example.mydiary.controller;

import com.example.mydiary.entity.Member;
import com.example.mydiary.service.GoogleMapService;
import com.example.mydiary.service.UserService;
import com.example.mydiary.service.WeatherService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

@ControllerAdvice
@AllArgsConstructor
public class ShareController {
    private WeatherService weatherService;
    private final GoogleMapService googleMapService;
    private final UserService userService;

    // 모든 템플릿에서 날씨, 지도 공통으로 적용
    @ModelAttribute
    public void addWeatherToModel(Model model) {
        String weather = weatherService.getWeather("Seoul");
        String mapApiKey = googleMapService.getApiKey();

        System.out.println("=========================현재 날씨: " + weather);
        model.addAttribute("weatherNow", weather);
        System.out.println("=========================현재 위치: " + mapApiKey);
        model.addAttribute("googleMapApiKey", mapApiKey);
    }

    // 모든 템플릿에서 사용자 정보 공통으로 적용
    @ModelAttribute("user")
    public void addUserToModel(HttpSession session, Model model) {
        Member user = (Member) session.getAttribute("user");

        // 프로필 사진 이미지가 비어있으면 디폴트 이미지 보여주기
        if (user != null) {
            if (user.getUImage() == null || user.getUImage().isEmpty()) {
                user.setUImage("/image/defaultProfileImage.webp");
            }
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("timestamp", System.currentTimeMillis());
    }
}
