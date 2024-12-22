package com.hust.documentweb.service.openai;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.documentweb.constant.CommonConstrant;

@Service
public class GeminiService {
    @Value("${openai.gemini.model}")
    private String model;

    @Value("${openai.gemini.apiKey}")
    private String apiKey;

    @Value("${openai.gemini.url}")
    private String url;

    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateContent(String content) throws JsonProcessingException {

        String geminiUrl = url + model + CommonConstrant.KEY + apiKey;

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Body
        Map<String, Object> parts = new HashMap<>();
        parts.put("text", content);

        Map<String, Object> contents = new HashMap<>();
        contents.put("parts", new Map[] {parts});

        Map<String, Object> body = new HashMap<>();
        body.put("contents", new Map[] {contents});

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send request
        ResponseEntity<String> response =
                restTemplate.exchange(geminiUrl, HttpMethod.POST, requestEntity, String.class);

        return getContent(response.getBody());
    }

    private String getContent(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // Trích xuất phần tử "candidates"
        JsonNode candidates = rootNode.path("candidates");
        String extractedText = ""; // Biến để lưu trữ nội dung "text"

        if (candidates.isArray() && candidates.size() > 0) {
            // Lấy "content" từ phần tử đầu tiên của mảng "candidates"
            JsonNode content = candidates.get(0).path("content").path("parts");

            for (JsonNode part : content) {
                extractedText += part.path("text").asText() + "\n";
            }
        } else {
            System.out.println("No content available.");
        }
        return extractedText;
    }
}
