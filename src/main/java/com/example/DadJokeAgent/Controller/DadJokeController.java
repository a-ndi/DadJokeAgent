package com.example.DadJokeAgent.Controller;

import com.example.DadJokeAgent.Service.DadJokeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Removed unnecessary imports for HttpServletRequest and Objects

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
        // Extract text properly from A2A JSON (Logic remains the same)
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


        Object incomingId = payload.getOrDefault("id", null);

        Map<String, Object> response = new HashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", incomingId);
        response.put("result", messageObject);

        System.out.println(" Outgoing A2A response: " + response);

        return ResponseEntity.ok()
                .header("Connection", "close")
                .body(response);
    }


    @GetMapping("/health")
    public String healthCheck(){
        return "DadJokeAgent is running!";
    }
}