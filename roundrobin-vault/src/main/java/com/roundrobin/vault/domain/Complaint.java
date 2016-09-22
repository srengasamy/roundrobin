package com.roundrobin.vault.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "complaint")
public class Complaint {
  private String id;
  @DBRef
  private UserProfile userProfile;
  @DBRef
  private UserProfile vendorProfile;
  private ComplaintType type;
  @DBRef
  private Job job;
  private String desc;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UserProfile getUserProfile() {
    return userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  public UserProfile getVendorProfile() {
    return vendorProfile;
  }

  public void setVendorProfile(UserProfile vendorProfile) {
    this.vendorProfile = vendorProfile;
  }

  public ComplaintType getType() {
    return type;
  }

  public void setType(ComplaintType type) {
    this.type = type;
  }

  public Job getJob() {
    return job;
  }

  public void setJob(Job job) {
    this.job = job;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public static enum ComplaintType {
    USER, VENDOR
  }
}
