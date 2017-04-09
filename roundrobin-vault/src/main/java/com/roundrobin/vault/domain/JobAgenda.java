package com.roundrobin.vault.domain;

import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
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
  private List<AvailableSlot> available = new ArrayList<>();
  private LocalDate date;

}
