package com.roundrobin.vault.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job_avoided")
public class JobAvoided {
  @Id
  @Indexed
  private String id;
  @DBRef
  private Job job;
  @DBRef
  private UserProfile vendor;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Job getJob() {
    return job;
  }

  public void setJob(Job job) {
    this.job = job;
  }

  public UserProfile getVendor() {
    return vendor;
  }

  public void setVendor(UserProfile vendor) {
    this.vendor = vendor;
  }

}
