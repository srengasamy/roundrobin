package com.roundrobin.gate.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * Created by rengasu on 5/8/17.
 */
@Data
@Document(collection = "job_slot")
public class JobSlot {

  @Id
  private String id;

  private boolean free;

  private DateTime start;

  private DateTime end;

  private short duration;

  @DBRef
  private Job job;
}
