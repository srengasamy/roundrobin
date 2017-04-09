package com.roundrobin.core.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
  @JsonProperty("user_id")
  private String userId;

  @JsonProperty("user_name")
  private String username;

  private boolean verified;

}
