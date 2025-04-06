package com.gofit.assistantservice;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecupService {

    private final JsonService jsonService;

    @SneakyThrows
    public String getRecupMessage(Client client) {
        // Simulate a call to an external service
        return "Recup message for client: " + client.getUserId();
    }
}
