package com.roundrobin.vault.domain;

import org.joda.time.DateTime;

import lombok.Data;

@Data
public class JobTimeline {
  private DateTime created;
  private DateTime scheduled;
  private DateTime completed;
  private DateTime start;
  private DateTime end;

}
