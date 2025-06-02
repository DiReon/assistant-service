package com.gofit.assistantservice.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile("!test")
public class FirebaseConfig {

  @Autowired
  private Environment environment;

  @Bean
  FirebaseApp firebaseApp() throws IOException {
    InputStream serviceAccount;
    FirebaseOptions options;
    if (isProductionProfile()) {
      log.info("Using production Firebase configuration");
      serviceAccount = new FileInputStream("/etc/secrets/serviceAccountKey_regina_go.json");
      options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .setDatabaseUrl("https://regina-go.firebaseio.com")
          .build();
    } else {
      log.info("Using development Firebase configuration");
      serviceAccount = new ClassPathResource("serviceAccountKey.json").getInputStream();
      options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .setDatabaseUrl("https://reginarus-go-default-rtdb.firebaseio.com")
          .build();
    }

    return FirebaseApp.initializeApp(options);
  }

  private boolean isProductionProfile() {
    for (String profile : environment.getActiveProfiles()) {
      if (profile.equals("prod")) {
        return true;
      }
    }
    return false;
  }

  @Bean
  FirebaseDatabase firebaseDatabase() throws IOException {
    return FirebaseDatabase.getInstance(firebaseApp());
  }
}