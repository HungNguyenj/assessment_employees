package com.example.assessment_employees.service;

import com.example.assessment_employees.repository.SentimentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SentimentService {

    @Autowired
    private RestTemplate restTemplate;

    public SentimentResponse analyzeComment(String comment) {
        String url = "http://localhost:5000/api/sentiment";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", comment);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<SentimentResponse> response = restTemplate
                    .postForEntity(url, request, SentimentResponse.class);
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Gọi API sentiment thất bại: " + e.getMessage());
            return null;
        }
    }
}

