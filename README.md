# DadJokeAgent


A lighthearted AI agent built with Spring Boot for the HNG Stage 3 Backend Task.
DadJokeAgent integrates with Telex.im and responds to chat messages with random dad jokes fetched from the icanhazdadjoke API.







## Overview



DadJokeAgent listens for messages sent to its webhook endpoint.
When a user’s message includes the words “joke” or “funny,” it fetches a random dad joke and responds in JSON format.
If the message is empty or unrelated, it gives a friendly nudge to ask for a joke.







## Tech Stack

- Java 17+
- Spring Boot 3
- Maven
- RestTemplate
- Telex.im A2A JSON protocol
- icanhazdadjoke.com API







### Endpoints
| Method | Endpoint | Description |
|--------|-----------|-------------|
| GET | /joker/health | Health check |
| POST | /joker/hook | Telex webhook |



### How It Works

Telex sends a message payload to /joker/hook:

{ "text": "tell me a joke" }



**The agent validates the payload:**

- Empty or missing text → “ I didn’t receive any message…”
- Contains “joke” or “funny” → Fetches a random dad joke
- Anything else → “Hey there! Ask me for a dad joke (try typing 'tell me a joke')”

Responds in this format:

{
  "status": "success",
  "message": "I used to play piano by ear, but now I use my hands.",
  "type": "text"
}

---

### SetUp
Clone the repository: git clone cd DadJokeAgent




---

<img width="1037" height="816" alt="image" src="https://github.com/user-attachments/assets/e1f16a4c-7911-4f64-97b4-e227d02c1ad3" />
