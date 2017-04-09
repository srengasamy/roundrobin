package com.roundrobin.test.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UnauthorizedError {
  private String error;

  @JsonProperty("error_description")
  private String description;

}
