package com.gofit.assistantservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.IgnoreExtraProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IgnoreExtraProperties
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
    private String activityId;
    private long dateCreated;
    private String title;
    private Integer points;
    private Integer monthTotalPoints;
    private String eventPhotoUrl;
    private Integer duration;
    private Integer kkal;
    private String thumbnailUrl;
    private String complexity;
}