package com.roundrobin.core.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  @JsonProperty("user_name")
  private String userId;

  @JsonProperty("authorities")
  private List<String> roles = new ArrayList<>();

  @JsonProperty("scope")
  private List<String> scopes = new ArrayList<>();

  @JsonProperty("aud")
  private List<String> resources = new ArrayList<>();

  private boolean verified;

}
