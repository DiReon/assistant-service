package com.gofit.assistantservice.utils;

import com.gofit.assistantservice.Activity;
import com.gofit.assistantservice.Client;
import com.gofit.assistantservice.JournalRecord;
import com.gofit.assistantservice.PaymentStatus;
import java.time.LocalDate;
import java.util.HashMap;

public class ClientGenerator {

    public static Client generateMockClient(String fakeId) {
        return Client.builder()
            .userId(fakeId)
            .email("test@example.com")
            .name("John")
            .lastName("Doe")
            .birthday(LocalDate.of(1990, 1, 1).toString())
            .sex("Male")
            .contactNumber("1234567890")
            .city("Test City")
            .informationSource("Internet")
            .source("Referral")
            .paymentStatus(PaymentStatus.CONFIRMED.name())
            .photoUrl("http://example.com/photo.jpg")
            .journal(new HashMap<>())
            .lastWeight(70.0)
            .height(175.0)
            .completedTrainings(new HashMap<>())
            .completedPrograms(new HashMap<>())
            .completedLectures(new HashMap<>())
            .goal("Fitness")
            .physicalActivity("3.0")
            .endOfAccessTimestamp(System.currentTimeMillis() + 1000000)
            .hasNewNotifications(false)
            .points(100)
            .trainerComments("Keep up the good work!")
            .excludeFromControl(false)
            .build();
    }

    public static JournalRecord generateMockJournalRecord(String date) {
        return JournalRecord.builder()
            .date(date)
            .activities(new HashMap<>())
            .build();
    }

    public static Activity generateMockActivity(String title) {
        return Activity.builder()
            .title(title)
            .build();
    }
}