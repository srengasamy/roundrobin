package com.roundrobin.vault.domain;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "available_slot")
public class AvailableSlot {
  @Id
  @Indexed
  private String id;
  private LocalTime start;
  private LocalTime end;
  private int minutes;

  public AvailableSlot() {

  }

  public AvailableSlot(LocalTime start, LocalTime end) {
    this.start = start;
    this.end = end;
    this.minutes = Minutes.minutesBetween(start, end).getMinutes();
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

  public int getMinutes() {
    return minutes;
  }

  public void setMinutes(int minutes) {
    this.minutes = minutes;
  }

}
