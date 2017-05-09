package com.roundrobin.gate.domain;

import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
@Document(collection = "job_agenda")
public class JobAgenda {
  @Id
  private String id;

  @DBRef
  private UserProfile profile;

  private List<JobSlot> slots = new ArrayList<>();

  private LocalDate date;

}
