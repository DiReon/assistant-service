package com.gofit.assistantservice;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FirebaseRepository {

    private final FirebaseDatabase firebaseDatabase;

    public CompletableFuture<Client> getById(String clientId) {
        var ref = firebaseDatabase.getReference("/users/" + clientId);
        CompletableFuture<Client> future = new CompletableFuture<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Client client = dataSnapshot.getValue(Client.class);
                    future.complete(client);
                } else {
                    future.complete(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return future;
    }

    /**
     * Saves a message to the Firebase database under the specified client ID.
     */
    public CompletableFuture<Void> saveMessage(String clientId, ChatMessage message) {
        log.info("Saving message for clientId: {}, message: {}", clientId, message);
        CompletableFuture<Void> future = new CompletableFuture<>();
        var ref = firebaseDatabase.getReference("/conversations/" + clientId);
        Map<String, Object> messageMap = Map.of(String.valueOf(message.getDateCreated()), message);
        ref.updateChildren(messageMap, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                log.error("Failed to save message for clientId: {}: {}", clientId, databaseError.getMessage());
                future.completeExceptionally(databaseError.toException());
            } else {
                log.info("Message saved successfully for clientId: {}", clientId);
                future.complete(null);
            }
        });
        return future;
    }
}
