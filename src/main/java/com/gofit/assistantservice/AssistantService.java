package com.gofit.assistantservice;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssistantService {

  private final FirebaseRepository firebaseRepository;

  private final RecupService recupService;

  private final ChatService chatService;

  public CompletableFuture<String> getMessageForClient(String clientId) {
    return firebaseRepository.getById(clientId)
        .thenApply(client -> {
          if (client != null) {
            log.info("Successfully returning data for userId: {}", clientId);
            return recupService.getRecupMessage(client);
          } else {
            log.warn("User data not found for clientId: {}", clientId);
            return "User data not found for ID: " + clientId;
          }
        });
  }

  public void respondToUserMessage(ChatMessage userMessage) {
    firebaseRepository.saveMessage(userMessage.getAuthorId(), userMessage).thenApply(result -> {
      log.info("Message saved successfully for userId: {}", userMessage.getAuthorId());
      return result;
    }).exceptionally(ex -> {
      log.error("Failed to save message for userId: {}: {}", userMessage.getAuthorId(), ex.getMessage(), ex);
      return null;
    });

    String response = chatService.getResponseToUserMessage(userMessage);
    ChatMessage chatMessage = ChatMessage.builder()
        .text(response)
        .type("assistant")
        .dateCreated(Instant.now().toEpochMilli())
        .build();
    firebaseRepository.saveMessage(userMessage.getAuthorId(), chatMessage).thenApply(result -> {
      log.info("Message saved successfully for userId: {}", userMessage.getAuthorId());
      return result;
    }).exceptionally(ex -> {
      log.error("Failed to save message for userId: {}: {}", userMessage.getAuthorId(), ex.getMessage(), ex);
      return null;
    });
  }
}
