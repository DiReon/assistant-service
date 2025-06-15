package com.gofit.assistantservice.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gofit.assistantservice.models.ChatMessage;
import com.gofit.assistantservice.services.AssistantService;
import com.gofit.assistantservice.services.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AssistantController {

  private final AssistantService assistantService;

  private final ChatService chatService;

  @GetMapping("/recap/{clientId}")
  public CompletableFuture<ResponseEntity<?>> getMessageForClient(@PathVariable String clientId) {
    return assistantService.getMessageForClient(clientId)
        .thenApply(message -> {
          if (message != null) {
            return ResponseEntity.ok(message);
          } else {
            return ResponseEntity.notFound().build();
          }
        })
        .exceptionally(ex -> {
          log.error("Error fetching user data for clientId: {}: {}", clientId, ex.getMessage(), ex);
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Error fetching data: " + ex.getMessage());
        });
  }

  @PostMapping("/chat")
  public ResponseEntity<Void> replyToMessage(@RequestBody ChatMessage userMessage) {
    assistantService.respondToUserMessage(userMessage);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/praise")
  public ResponseEntity<Info> getPraiseMessage(@RequestParam String clientInfo) {
      String message = chatService.getPraiseMessage(clientInfo);
      return ResponseEntity.ok(new Info(message));
  }
  
  
  @GetMapping("/info")
  public String getInfo() {
      return new String("Info");
  }
  
  record Info(String message) {}

}
