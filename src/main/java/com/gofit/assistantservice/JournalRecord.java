package com.gofit.assistantservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IgnoreExtraProperties
@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalRecord {
    private String date;
    private Long dateRecorded;
    private Integer kkal;
    private Boolean isKkalInRange;
    private Integer steps;
    private String[] trainingTitles;
    private Map<String, Activity> activities;
    private Map<String, String> lectureTitles;
    private String[] articleTitles;
    private String activity;
    private Double weight;
    private Double percentage;
    private String notes;
    private Integer checkListPoints;
}

