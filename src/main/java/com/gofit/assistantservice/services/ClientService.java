package com.gofit.assistantservice.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.gofit.assistantservice.models.Client;
import com.gofit.assistantservice.repository.FirebaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
  private final FirebaseRepository firebaseRepository;

  public CompletableFuture<Client> getClientById(String clientId) {
    return firebaseRepository.getById(clientId);
  }

}
