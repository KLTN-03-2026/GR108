package com.example.demo.controller;

import com.example.demo.entity.ChatbotLog;
import com.example.demo.repository.IChatbotLogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private final IChatbotLogRepository chatbotLogRepository;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    private static final String SYSTEM_PROMPT =
            "Bạn là trợ lý AI của hệ thống ký túc xá sinh viên. " +
                    "Hãy trả lời ngắn gọn, thân thiện bằng tiếng Việt.";

    public ChatbotController(IChatbotLogRepository chatbotLogRepository) {

        this.chatbotLogRepository = chatbotLogRepository;

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(10000);
        factory.setReadTimeout(30000);

        this.restTemplate = new RestTemplate(factory);

        this.objectMapper = new ObjectMapper();
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(
            @RequestBody Map<String, String> body
    ) {

        String question =
                body.getOrDefault("question", "").trim();

        if (question.isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "answer",
                            "Vui lòng nhập câu hỏi."
                    ));
        }

        try {

            String answer =
                    callGroqAPI(question);

            saveChatLog(question, answer);

            return ResponseEntity.ok(
                    Map.of("answer", answer)
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.ok(
                    Map.of(
                            "answer",
                            "Lỗi AI: " + e.getMessage()
                    )
            );
        }
    }

    private String callGroqAPI(String question)
            throws Exception {

        String url =
                "https://api.groq.com/openai/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        // QUAN TRỌNG
        headers.setBearerAuth(groqApiKey);

        ArrayNode messages =
                objectMapper.createArrayNode();

        ObjectNode systemMessage =
                objectMapper.createObjectNode();

        systemMessage.put("role", "system");

        systemMessage.put(
                "content",
                SYSTEM_PROMPT
        );

        ObjectNode userMessage =
                objectMapper.createObjectNode();

        userMessage.put("role", "user");

        userMessage.put("content", question);

        messages.add(systemMessage);

        messages.add(userMessage);

        ObjectNode requestBody =
                objectMapper.createObjectNode();

        requestBody.put(
                "model",
                "llama-3.3-70b-versatile"
        );

        requestBody.set("messages", messages);

        requestBody.put("temperature", 0.7);

        requestBody.put("max_tokens", 512);

        String jsonBody =
                objectMapper.writeValueAsString(requestBody);

        System.out.println("===== REQUEST =====");

        System.out.println(jsonBody);

        HttpEntity<String> entity =
                new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        System.out.println("===== RESPONSE =====");

        System.out.println(response.getBody());

        String responseBody = response.getBody();

        if (responseBody == null
                || responseBody.isEmpty()) {

            return "Không nhận được phản hồi từ AI.";
        }

        JsonNode root =
                objectMapper.readTree(responseBody);

        JsonNode choices =
                root.path("choices");

        if (!choices.isArray()
                || choices.isEmpty()) {

            return "AI không trả về dữ liệu.";
        }

        return choices
                .get(0)
                .path("message")
                .path("content")
                .asText("AI không có phản hồi.");
    }

    private void saveChatLog(
            String question,
            String answer
    ) {

        try {

            ChatbotLog log =
                    new ChatbotLog();

            log.setQuestion(question);

            log.setAnswer(answer);

            log.setCreatedAt(
                    LocalDateTime.now()
            );

            chatbotLogRepository.save(log);

        } catch (Exception ignored) {
        }
    }
}