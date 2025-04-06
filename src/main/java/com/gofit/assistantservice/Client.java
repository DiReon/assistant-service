package com.gofit.assistantservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IgnoreExtraProperties()
@com.google.cloud.firestore.annotation.IgnoreExtraProperties
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {
    private String userId;
    private String email;
    private String registrationDate;
    private String name;
    private String lastName;
    private String birthday;
    private String sex;
    private String contactNumber;
    private String city;
    private String informationSource;
    private String source;
    private PaymentStatus paymentStatus;
    private String photoUrl;
    private Map<String, Object> journal;
    private double lastWeight;
    private double height;
    private Map<String, String> completedTrainings;
    private Map<String, Integer> completedPrograms;
    private Map<String, String> completedLectures;
    private String goal;
    private String physicalActivity;
    private long endOfAccessTimestamp;
    private boolean hasNewNotifications;
    private int points;
    private String trainerComments;
    private boolean excludeFromControl;
}