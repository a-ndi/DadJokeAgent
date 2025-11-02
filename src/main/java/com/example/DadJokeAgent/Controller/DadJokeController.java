package com.example.DadJokeAgent.Controller;


import com.example.DadJokeAgent.Service.DadJokeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("joker")
public class DadJokeController {

    private final DadJokeService dadJokeService;

    public DadJokeController(DadJokeService dadJokeService){
        this.dadJokeService = dadJokeService;
    }



    @PostMapping(value = "/hook", consumes = "application/json", produces = "application/json")

    public Map<String , Object> handleTelexMsg(@RequestBody (required = false)Map<String , Object>payload,  HttpServletRequest request){
        System.out.println("---- Incoming Request Details ----");
        System.out.println("HTTP Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Content-Type: " + request.getContentType());
        System.out.println("----------------------------------");
        String userText = String.valueOf(payload.getOrDefault("text", "")).toLowerCase();

        String responseText;
        if (userText.isEmpty() || userText.equals("null")) {
            responseText = " I didn’t receive any message. Please include some text, e.g. 'tell me a joke'.";
        } else if (userText.contains("joke") || userText.contains("funny")) {

            responseText = dadJokeService.getRandomJoke();
        } else {
            responseText = "Hey there! Ask me for a dad joke (try typing 'tell me a joke')";
        }



        Map<String , Object> response= new HashMap<>();
        response.put("status" , "success");
        response.put("message" , responseText);
        response.put("type", "text");

        return response;
    }

    @GetMapping("/health")

    public String healthCheck(){
        return "DadJokeAgent is running!";
    }


}
