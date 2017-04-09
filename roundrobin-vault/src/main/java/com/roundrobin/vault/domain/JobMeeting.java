package com.roundrobin.vault.domain;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
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

}
