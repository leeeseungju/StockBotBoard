package com.example.stockboard.client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class FastApiClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> requestReport(String message) {
        String url = "http://stockbot:8000/report";

        // JSON 형태로 보낼 데이터
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("message", message);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 엔티티 구성
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // POST 요청
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return response.getBody();
    }
}
