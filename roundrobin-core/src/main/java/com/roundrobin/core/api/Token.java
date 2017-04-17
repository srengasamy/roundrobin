package com.roundrobin.core.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by rengasu on 4/16/17.
 */
@Data
public class Token {
  @JsonProperty("user_name")
  private String userId;

  private List<String> roles = new ArrayList<>();

  private boolean verified;

}
