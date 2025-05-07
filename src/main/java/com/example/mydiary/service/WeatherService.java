package com.example.mydiary.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class WeatherService {

    // 날씨 APi 키 설정
    @Value("${weather.api.key}")
    private String apiKey;

    // 날씨 API 호출
    public String getWeather(String location) {
        try {
            String url = String.format(
                    "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/%s?unitGroup=metric&key=%s&contentType=json",
                    location, apiKey);

            RestTemplate restTemplate = new RestTemplate();
            System.out.println("+++++ 날씨 API 호출 URL: " + url);

            String response = restTemplate.getForObject(url, String.class);
            System.out.println("+++++ API 응답: " + response);

            return parseWeatherNowString(response, location);
        } catch (Exception e) {
            e.printStackTrace();
            return "날씨 정보를 불러올 수 없습니다.";
        }
    }

    // 응답 JSON 파싱
    private String parseWeatherNowString(String response, String location) {
        JSONObject json = new JSONObject(response);
        JSONArray days = json.getJSONArray("days");
        JSONObject today = days.getJSONObject(0);

        String conditionRaw = today.optString("conditions", "정보 없음").toLowerCase();
        double temp = today.optDouble("temp", 0);

        // 한글로 변환
        Map<String, String> conditionKoList = new HashMap<>();
        conditionKoList.put("clear", "맑음");
        conditionKoList.put("partially cloudy", "부분 흐림");
        conditionKoList.put("rain", "비");
        conditionKoList.put("snow", "눈");
        conditionKoList.put("overcast", "흐림");
        conditionKoList.put("cloudy", "흐림");
        conditionKoList.put("thunderstorm", "천둥번개");
        conditionKoList.put("fog", "안개");
        conditionKoList.put("drizzle", "이슬비");
        conditionKoList.put("windy", "바람 많음");

        String[] conditions = conditionRaw.split(", ");
        StringBuilder conditionKo = new StringBuilder();
        for (String c : conditions) {
            String trimmed = c.trim();
            String translated = conditionKoList.getOrDefault(trimmed, trimmed);
            if (conditionKo.length() > 0) {
                conditionKo.append(", ");
            }
            conditionKo.append(translated);
        }

        return String.format("%s의 현재 날씨: %s, %.1f°C", location, conditionKo.toString(), temp);
    }
}
