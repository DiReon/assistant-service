package com.gofit.assistantservice;

import java.time.LocalDate;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RecupService {

    private final JsonService jsonService;

    private final ChatClient chatClient;

    public RecupService(ChatClient.Builder chatClientBuilder, JsonService jsonService) {
        this.chatClient = chatClientBuilder.build();
        this.jsonService = jsonService;
    }

    @SneakyThrows
    public String getRecupMessage(Client client) {
        List<JournalRecord> records = client.getJournal().values().stream()
            .filter(item -> LocalDate.parse(item.getDate()).isAfter(LocalDate.now().minusDays(7)))
            .toList();
        log.info("Client journal records: {}", records);
        String clientInfo = client.getName() + " :";
        String journal = jsonService.toJson(records);
        String generation = chatClient
            .prompt()
            .system(RecupAssistantSystemMessage.content)
            .user(clientInfo + journal)
            .call()
            .content();
        log.info(generation);
        return "Recup message for client: " + generation;
//        return jsonService.toJson(client);
    }
}
