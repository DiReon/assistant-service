package com.gofit.assistantservice.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AssistantTools {

  @Tool(description = "Send the user the button to renew subscription", returnDirect = true)
  public String sendTheButtonToRenewSubscription() {
    log.info("Renewing subscription...");
    return "{{button-to-renew-subscription}}";
  }

  @Tool(description = "Get the today date in the format YYYY-MM-DD", returnDirect = true)
  public String getTodayDate() {
    String todayDate = java.time.LocalDate.now().toString();
    log.info("Today date is: {}", todayDate);
    return todayDate;
  }
}
