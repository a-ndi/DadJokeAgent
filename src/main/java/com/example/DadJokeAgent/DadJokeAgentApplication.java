package com.example.DadJokeAgent;

import org.springframework.context.ApplicationContext;
import com.example.DadJokeAgent.Service.DadJokeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DadJokeAgentApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DadJokeAgentApplication.class, args);

		DadJokeService dadJokeService = context.getBean(DadJokeService.class);
		String jokeResponse = dadJokeService.getRandomJoke();
		System.out.println("Dad Joke API Response: " + jokeResponse);
	}

}
