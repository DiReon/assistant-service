package com.gofit.assistantservice;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String getResponseToUserMessage(ChatMessage userMessage) {
        return chatClient
            .prompt()
            .system(ChatAssistantSystemMessage.content)
            .user(userMessage.getText())
            .call()
            .content();
    }
}
