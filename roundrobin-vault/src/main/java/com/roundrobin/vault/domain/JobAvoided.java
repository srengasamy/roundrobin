package com.roundrobin.vault.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "job_avoided")
public class JobAvoided {
  @Id
  @Indexed
  private String id;
  @DBRef
  private Job job;
  @DBRef
  private UserProfile vendor;

}
