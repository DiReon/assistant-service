package com.gofit.assistantservice.models;

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
    private Long registrationDate;
    private String name;
    private String lastName;
    private String birthday;
    private String sex;
    private String contactNumber;
    private String city;
    private String informationSource;
    private String source;
    private String paymentStatus;
    private String photoUrl;
    private Map<String, JournalRecord> journal;
    private Double lastWeight;
    private Double height;
    private Map<String, String> completedTrainings;
    private Map<String, Integer> completedPrograms;
    private Map<String, String> completedLectures;
    private String goal;
    private String physicalActivity;
    private Long endOfAccessTimestamp;
    private Boolean hasNewNotifications;
    private Integer points;
    private String trainerComments;
    private Boolean excludeFromControl;
}