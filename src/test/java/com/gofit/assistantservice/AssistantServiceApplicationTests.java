package com.gofit.assistantservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gofit.assistantservice.config.TestFirebaseConfig;

@SpringBootTest
@ActiveProfiles("test")
@ImportAutoConfiguration(TestFirebaseConfig.class)
class AssistantServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
