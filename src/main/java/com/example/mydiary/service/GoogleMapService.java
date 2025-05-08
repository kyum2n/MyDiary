package com.example.mydiary.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleMapService {

    @Value("${google.map.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
