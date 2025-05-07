package com.example.mydiary.controller;

import com.example.mydiary.service.WeatherService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
@AllArgsConstructor
public class ShareController {
    private WeatherService weatherService;

    // 모든 컨트롤러에서 날씨 공통으로 적용
    @ModelAttribute
    public void addWeatherToModel(Model model) {
        String weather = weatherService.getWeather("Seoul");
        System.out.println("=========================현재 날씨: " + weather);
        model.addAttribute("weatherNow", weather);
    }
}
