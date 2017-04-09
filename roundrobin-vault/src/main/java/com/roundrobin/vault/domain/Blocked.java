package com.roundrobin.vault.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "blocked")
public class Blocked {
  private String id;
  @DBRef
  private UserProfile user;
  @DBRef
  private UserProfile vendor;
  @DBRef
  private Complaint complaint;

}
