package com.gofit.assistantservice.services;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.gofit.assistantservice.models.ChatMessage;
import com.gofit.assistantservice.models.Client;
import com.gofit.assistantservice.repository.FirebaseRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssistantService {

  private final FirebaseRepository firebaseRepository;

  private final RecupService recupService;

  private final ChatService chatService;

  private final ClientService clientService;

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
    firebaseRepository.saveMessage(userMessage.getAuthorId(), userMessage).thenApply(logSuccessMessage(userMessage));

    Client client = null;
    if (userMessage.getAuthorId() != null) {
       client = clientService.getClientById(userMessage.getAuthorId())
        .join(); // Blocking call to ensure client data is available before proceeding
    }
    log.info("Client data retrieved for clientId: {}", userMessage.getAuthorId());
    if (client == null) {
      log.warn("Client not found for userId: {}", userMessage.getAuthorId());
    }
    String response = chatService.getResponseToUserMessage(userMessage, client);
    ChatMessage chatMessage = ChatMessage.builder()
        .text(response)
        .type("assistant")
        .dateCreated(Instant.now().toEpochMilli())
        .build();
    firebaseRepository.saveMessage(userMessage.getAuthorId(), chatMessage).thenApply(logSuccessMessage(userMessage));
  }

  private Function<? super Void, ? extends Void> logSuccessMessage(ChatMessage userMessage) {
    return result -> {
      log.info("Message saved successfully for userId: {}", userMessage.getAuthorId());
      return result;
    };
  }
}
