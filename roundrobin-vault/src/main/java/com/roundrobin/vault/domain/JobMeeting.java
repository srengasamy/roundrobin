package com.roundrobin.vault.domain;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job_meeting")
public class JobMeeting {
  @Id
  @Indexed
  private String id;
  @DBRef
  private Job job;
  @DBRef
  private UserProfile userProfile;
  private LocalDate created;
  private LocalTime start;
  private LocalTime end;

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

  public LocalTime getStart() {
    return start;
  }

  public void setStart(LocalTime start) {
    this.start = start;
  }

  public LocalTime getEnd() {
    return end;
  }

  public void setEnd(LocalTime end) {
    this.end = end;
  }

  public UserProfile getUserProfile() {
    return userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  public LocalDate getCreated() {
    return created;
  }

  public void setCreated(LocalDate created) {
    this.created = created;
  }

}
