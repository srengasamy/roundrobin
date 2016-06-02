package com.roundrobin.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Response<T> {
  private long timestamp;
  private String uuid;
  private T entity;
  private List<Error> errors = new ArrayList<>();

  public Response(T entity) {
    this.entity = entity;
    timestamp = System.currentTimeMillis();
  }

  public Response(List<Error> errors) {
    this.timestamp = System.currentTimeMillis();
    this.errors = errors;
  }

  public Response(Error error) {
    this.timestamp = System.currentTimeMillis();
    this.errors.add(error);
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

  public List<Error> getErrors() {
    return errors;
  }

  public void setErrors(List<Error> errors) {
    this.errors = errors;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

}
