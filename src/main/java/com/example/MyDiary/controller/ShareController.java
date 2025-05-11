package com.example.mydiary.controller;

import com.example.mydiary.service.GoogleMapService;
import com.example.mydiary.service.UserService;
import com.example.mydiary.service.WeatherService;

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
    public void addUserToModel(Authentication authentication, Model model) {

        if (authentication != null && authentication.getName() != null) {
            String uId = authentication.getName();
            com.example.mydiary.entity.Member user = userService.findUserByUId(uId);
            model.addAttribute("user", user);
        }
    }
}
