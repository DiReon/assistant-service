package com.gofit.assistantservice;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AssistantController {

  private final AssistantService assistantService;

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

}
