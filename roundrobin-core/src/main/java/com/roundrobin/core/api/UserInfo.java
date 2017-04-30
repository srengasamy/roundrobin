package com.roundrobin.core.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by rengasu on 4/25/17.
 */
@Data
public class UserInfo {
  @JsonProperty("user_id")
  private String userId;

  private String username;

  private List<String> roles = new ArrayList<>();

  private boolean verified;
}
