package com.gofit.assistantservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {
  private String id;
  private String text;
  private String authorId;
  private String type;
  private long dateCreated;
}
