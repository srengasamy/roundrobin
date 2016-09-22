package com.roundrobin.vault.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blocked")
public class Blocked {
  private String id;
  @DBRef
  private UserProfile user;
  @DBRef
  private UserProfile vendor;
  @DBRef
  private Complaint complaint;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UserProfile getUser() {
    return user;
  }

  public void setUser(UserProfile user) {
    this.user = user;
  }

  public UserProfile getVendor() {
    return vendor;
  }

  public void setVendor(UserProfile vendor) {
    this.vendor = vendor;
  }

  public Complaint getComplaint() {
    return complaint;
  }

  public void setComplaint(Complaint complaint) {
    this.complaint = complaint;
  }

}
