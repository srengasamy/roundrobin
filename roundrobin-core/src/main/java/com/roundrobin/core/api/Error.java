package com.roundrobin.core.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Error {
  private String type;
  private String code;
  private String message;
  private String param;

  @JsonProperty("field_errors")
  private List<String> fieldErrors;

}
