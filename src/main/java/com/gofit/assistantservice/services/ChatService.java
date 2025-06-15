package com.gofit.assistantservice.services;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import com.gofit.assistantservice.models.ChatMessage;
import com.gofit.assistantservice.models.Client;
import com.gofit.assistantservice.prompts.ChatAssistantSystemMessage;
import com.gofit.assistantservice.prompts.PraiseAssistantSystemMessage;

@Service
public class ChatService {
  private final ChatClient chatClient;
  private final ChatMemory chatMemory;

  public ChatService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
    this.chatClient = chatClientBuilder
        .build();
    this.chatMemory = chatMemory;
  }

  public String getResponseToUserMessage(ChatMessage userMessage, Client client) {
    String clientDataString = "";
    if (client != null) {
      clientDataString = String.format(
          "Available Client data: name: %s, sex: %s, birthdate: %s, subscription is active: %s, instructor comments: %s, goal: %s, last weight: %s, height: %s",
          client.getName(),
          client.getSex(),
          client.getBirthday(),
          client.isActive(),
          client.getTrainerComments(),
          client.getGoal(),
          client.getLastWeight(),
          client.getHeight());
    }

    chatMemory.add(userMessage.getAuthorId(), new UserMessage(userMessage.getText()));
    var history = chatMemory.get(userMessage.getAuthorId(), 50);
    List<Message> messages = new ArrayList<>(history.isEmpty() ? List.of() : history);
    String todayDateString = "Today date is: " + java.time.LocalDate.now().toString();
    messages.add(new SystemMessage(ChatAssistantSystemMessage.content + clientDataString + todayDateString));
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

  public String getPraiseMessage(String clientInfo) {
    String prompt = PraiseAssistantSystemMessage.content + " Training session info: " + clientInfo;
    ChatOptions chatOptions = ChatOptions.builder()
        .temperature(1.0)
        .build();
    return chatClient.prompt(new Prompt(prompt, chatOptions)).call().content();
  }
}
