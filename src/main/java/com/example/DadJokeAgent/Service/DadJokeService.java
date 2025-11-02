package com.example.DadJokeAgent.Service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class DadJokeService {

    private static final String DadJokeURL = "https://icanhazdadjoke.com/";

    public String getRandomJoke() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "DadJokeAgent (https://github.com/ndifrekeedem)");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(DadJokeURL, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String joke = root.path("joke").asText();

                if (joke == null || joke.isBlank()) {
                    return " Sorry, I couldn't find a good joke right now.";
                }

                return joke;
            } else {
                return " The Dad Joke API returned an unexpected response.";
            }

        } catch (IOException e) {
            return " Failed to parse the joke response.";
        } catch (Exception e) {
            return "Oops! My joke engine is down. Try again later.";
        }
}
}
