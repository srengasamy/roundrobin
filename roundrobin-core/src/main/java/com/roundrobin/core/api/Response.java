package com.roundrobin.core.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public T getEntity() {
    return entity;
  }

  public void setEntity(T entity) {
    this.entity = entity;
  }

  public Error getError() {
    return error;
  }

  public void setError(Error error) {
    this.error = error;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

}
