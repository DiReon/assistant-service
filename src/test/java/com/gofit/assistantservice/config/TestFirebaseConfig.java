package com.gofit.assistantservice.config;

import static org.mockito.Mockito.mock;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestFirebaseConfig {

    @Bean
    @Primary
    FirebaseApp testFirebaseApp() throws IOException {
        return mock(FirebaseApp.class);
    }

    @Bean
    @Primary
    FirebaseDatabase testFirebaseDatabase() {
      return mock(FirebaseDatabase.class);
    }
}