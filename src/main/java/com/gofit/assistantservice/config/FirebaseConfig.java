package com.gofit.assistantservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.io.FileInputStream;

@Configuration
@Profile("!test")
public class FirebaseConfig {

    @Bean
    FirebaseApp firebaseApp() throws IOException {
        InputStream serviceAccount = new FileInputStream("/etc/secrets/serviceAccountKey_regina_go.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://regina-go.firebaseio.com")
            .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    FirebaseDatabase firebaseDatabase() throws IOException {
        return FirebaseDatabase.getInstance(firebaseApp());
    }
}