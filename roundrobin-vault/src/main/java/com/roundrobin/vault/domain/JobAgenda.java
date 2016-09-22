package com.roundrobin.vault.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job_agenda")
public class JobAgenda {
  @Id
  @Indexed
  private String id;
  @DBRef
  private UserProfile vendor;
  @DBRef
  private List<JobMeeting> meetings = new ArrayList<>();
  @DBRef
  private List<AvailableSlot> availables = new ArrayList<>();
  private LocalDate date;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UserProfile getVendor() {
    return vendor;
  }

  public void setVendor(UserProfile vendor) {
    this.vendor = vendor;
  }

  public List<JobMeeting> getMeetings() {
    return meetings;
  }

  public void setMeetings(List<JobMeeting> meetings) {
    this.meetings = meetings;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public List<AvailableSlot> getAvailables() {
    return availables;
  }

  public void setAvailables(List<AvailableSlot> availables) {
    this.availables = availables;
  }

}
