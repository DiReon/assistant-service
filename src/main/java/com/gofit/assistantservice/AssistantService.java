package com.gofit.assistantservice;

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
}
