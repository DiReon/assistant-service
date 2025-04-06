package com.gofit.assistantservice;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.gofit.assistantservice.config.TestFirebaseConfig;
import com.gofit.assistantservice.utils.ClientGenerator;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
@Import({TestFirebaseConfig.class})
public class SmokeTest {

    @Autowired
    AssistantController assistantController;

    @MockitoBean
    FirebaseRepository firebaseRepository;

    private final String fakeId = "fakeId";
    static final String FAKE_RECAP_MESSAGE = "Recup message for client: fakeId";
    private CompletableFuture<Client> future;

    @BeforeEach
    void beforeEach() {
        Client mockClient = ClientGenerator.generateMockClient(fakeId);
        future = new CompletableFuture<>();
        CompletableFuture<Client> nullFuture = new CompletableFuture<>();
        when(firebaseRepository.getById(anyString())).thenReturn(nullFuture);
        when(firebaseRepository.getById(fakeId)).thenReturn(future);
        future.complete(mockClient);
        nullFuture.complete(null);
    }

    @SneakyThrows
    @Test
    void returnNotFoundIfClientIdIsNotFound() {
        var messageFuture = assistantController.getMessageForClient("id-does-not-exist");
        var message = messageFuture.get();
        assertThat(message.getBody()).isEqualTo("User data not found for ID: id-does-not-exist");
    }

    @SneakyThrows
    @Test
    void getRecapMessageForClient() {
        var messageFuture = assistantController.getMessageForClient(fakeId);
        var message = messageFuture.get();
        assertThat(message.getBody()).isEqualTo(FAKE_RECAP_MESSAGE);
        assertThat(AssistantController.class).isNotNull();
    }
}
