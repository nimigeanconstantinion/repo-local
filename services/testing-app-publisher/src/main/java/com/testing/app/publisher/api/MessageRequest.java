package com.testing.app.publisher.api;

import jakarta.validation.constraints.NotBlank;

public class MessageRequest {
  @NotBlank
  private String payload;

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }
}
