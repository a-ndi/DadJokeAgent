package com.example.DadJokeAgent.Controller;


import com.example.DadJokeAgent.Service.DadJokeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/joker")
public class DadJokeController {

    private final DadJokeService dadJokeService;

    public DadJokeController(DadJokeService dadJokeService){
        this.dadJokeService = dadJokeService;
    }



    @SuppressWarnings("unchecked")
    @PostMapping(value = "/hook", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> handleTelexMsg(@RequestBody Map<String, Object> payload) {
        // Extract text properly from A2A JSON
        String userText = "";

        try {
            Map<String, Object> params = (Map<String, Object>) payload.get("params");
            if (params != null && params.get("message") instanceof Map<?, ?> messageMap) {
                List<Map<String, Object>> parts = (List<Map<String, Object>>) messageMap.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    Object textObj = parts.get(0).get("text");
                    if (textObj != null) {
                        userText = textObj.toString().toLowerCase();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing user text: " + e.getMessage());
        }

        // Debug log
        System.out.println(" Incoming user text: '" + userText + "'");

        String responseText;
        if (userText.isEmpty() || userText.equals("null")) {
            responseText = "I didn’t receive any message. Please include some text, e.g. 'tell me a joke'.";
        } else if (userText.contains("joke") || userText.contains("funny")) {
            responseText = dadJokeService.getRandomJoke();
        } else {
            responseText = "Hey there! Ask me for a dad joke (try typing 'tell me a joke')";
        }

        Map<String, Object> messagePart = Map.of("kind", "text", "text", responseText);
        Map<String, Object> messageObject = Map.of(
                "role", "agent",
                "parts", List.of(messagePart),
                "kind", "message"
        );

        Map<String, Object> status = Map.of(
                "state", "completed",
                "timestamp", java.time.Instant.now().toString(),
                "message", messageObject
        );

        Map<String, Object> result = Map.of(
                "id", java.util.UUID.randomUUID().toString(),
                "contextId", java.util.UUID.randomUUID().toString(),
                "status", status,
                "kind", "task"
        );

        Map<String, Object> response = Map.of(
                "jsonrpc", "2.0",
                "id", payload.getOrDefault("id", null),
                "result", result
        );

        System.out.println("Outgoing response: " + response);

        return ResponseEntity.ok()
                .header("Connection", "close")
                .body(response);
    }


    @GetMapping("/health")

    public String healthCheck(){
        return "DadJokeAgent is running!";
    }


}
