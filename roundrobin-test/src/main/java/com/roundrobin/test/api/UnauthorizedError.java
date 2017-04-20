package com.roundrobin.test.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.util.StringUtils;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnauthorizedError {
  private String error;

  @JsonProperty("error_description")
  private String description;

  private String message;
}
