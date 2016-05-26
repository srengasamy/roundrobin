package com.roundrobin.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by rengasu on 5/25/16.
 */
@Document(collection = "role")
public class Role {
  @Id
  private String id;

  private String name;

  public Role() {

  }

  public Role(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }
}
