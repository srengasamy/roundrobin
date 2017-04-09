package com.roundrobin.core.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class Response<T> {
  private long timestamp;
  private String uuid;
  private T entity;
  private Error error;

  public Response(T entity) {
    if (entity instanceof Error) {
      this.error = (Error) entity;
    } else {
      this.entity = entity;
    }
    timestamp = System.currentTimeMillis();
  }

  public Response() {
    timestamp = System.currentTimeMillis();
  }


}
