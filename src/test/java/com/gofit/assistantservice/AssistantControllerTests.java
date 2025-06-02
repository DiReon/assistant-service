package com.gofit.assistantservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.gofit.assistantservice.config.TestFirebaseConfig;
import com.gofit.assistantservice.controllers.AssistantController;
import com.gofit.assistantservice.models.Activity;
import com.gofit.assistantservice.models.ChatMessage;
import com.gofit.assistantservice.models.Client;
import com.gofit.assistantservice.models.JournalRecord;
import com.gofit.assistantservice.repository.FirebaseRepository;
import com.gofit.assistantservice.services.ChatService;
import com.gofit.assistantservice.services.RecupService;
import com.gofit.assistantservice.utils.ClientGenerator;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
@Import({ TestFirebaseConfig.class })
public class AssistantControllerTests {

  @Autowired
  AssistantController assistantController;

  @MockitoBean
  FirebaseRepository firebaseRepository;

  @MockitoBean
  RecupService recupService;

  @MockitoBean
  ChatService chatService;

  private final String fakeId = "fakeId";
  static final String FAKE_RECAP_MESSAGE = "Recup message for client: fakeId";
  private CompletableFuture<Client> future;
  private String trainingTitle = "Здоровье-1";

  @BeforeEach
  void beforeEach() {
    Client mockClient = ClientGenerator.generateMockClient(fakeId);
    String dateString = LocalDate.now().minusDays(6).toString();
    JournalRecord mockJournalRecord = ClientGenerator.generateMockJournalRecord(dateString);
    Activity mockActivity = ClientGenerator.generateMockActivity(trainingTitle);
    mockJournalRecord.getActivities().put(String.valueOf(System.currentTimeMillis()), mockActivity);
    // mockClient.getJournal().put(dateString, mockJournalRecord);
    future = new CompletableFuture<>();
    CompletableFuture<Client> nullFuture = new CompletableFuture<>();
    when(firebaseRepository.getById(anyString())).thenReturn(nullFuture);
    when(firebaseRepository.getById(fakeId)).thenReturn(future);
    future.complete(mockClient);
    nullFuture.complete(null);
    when(recupService.getRecupMessage(any(Client.class))).thenReturn(FAKE_RECAP_MESSAGE);
    when(firebaseRepository.saveMessage(anyString(), any(ChatMessage.class)))
        .thenReturn(CompletableFuture.completedFuture(null));
  }

  @Test
  @SneakyThrows
  void returnNotFoundIfClientIdIsNotFound() {
    var messageFuture = assistantController.getMessageForClient("id-does-not-exist");
    var message = messageFuture.get();
    assertThat(message.getBody()).isEqualTo("User data not found for ID: id-does-not-exist");
  }

  @Test
  @SneakyThrows
  void getRecapMessageForClient() {
    var messageFuture = assistantController.getMessageForClient(fakeId);
    var message = messageFuture.get();
    assertThat(message.getBody()).isEqualTo(FAKE_RECAP_MESSAGE);
  }

  @Test
  @SneakyThrows
  void answerClientQuestionWithResponseFromAssistant() {
    var mockMessageText = "How to start training?";
    ChatMessage chatMessage = ChatMessage.builder()
        .authorId("fake-author-id")
        .text(mockMessageText)
        .build();
    var mockResponseText = "Pay for subscription first.";
    when(firebaseRepository.saveMessage(anyString(), any())).thenReturn(new CompletableFuture<>());
    when(chatService.getResponseToUserMessage(any())).thenReturn(mockResponseText);

    ResponseEntity<Void> assistantMessage = assistantController.replyToMessage(chatMessage);

    assertEquals(assistantMessage.getStatusCode(), ResponseEntity.ok().build().getStatusCode());
  }
}
