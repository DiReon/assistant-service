package com.gofit.assistantservice;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
  private final ChatClient chatClient;
  private final ChatMemory chatMemory;

  public ChatService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
    this.chatClient = chatClientBuilder
        .defaultSystem(ChatAssistantSystemMessage.content)
        .build();
    this.chatMemory = chatMemory;
  }

  public String getResponseToUserMessage(ChatMessage userMessage) {
    chatMemory.add(userMessage.getAuthorId(), new UserMessage(userMessage.getText()));
    var history = chatMemory.get(userMessage.getAuthorId(), 50);
    List<Message> messages = new ArrayList<>(history.isEmpty() ? List.of() : history);
    var assistantResponse = chatClient
        .prompt(new Prompt(messages))
        .advisors(a -> a
            .param(CHAT_MEMORY_CONVERSATION_ID_KEY, userMessage.getAuthorId())
            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
        .call()
        .content();
    chatMemory.add(userMessage.getAuthorId(), new AssistantMessage(assistantResponse));
    return assistantResponse;
  }
}
