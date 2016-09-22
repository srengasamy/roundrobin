package com.roundrobin.vault.domain;

import org.joda.time.DateTime;

public class JobTimeline {
  private DateTime created;
  private DateTime scheduled;
  private DateTime completed;
  private DateTime start;
  private DateTime end;

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public DateTime getScheduled() {
    return scheduled;
  }

  public void setScheduled(DateTime scheduled) {
    this.scheduled = scheduled;
  }

  public DateTime getCompleted() {
    return completed;
  }

  public void setCompleted(DateTime completed) {
    this.completed = completed;
  }

  public DateTime getStart() {
    return start;
  }

  public void setStart(DateTime start) {
    this.start = start;
  }

  public DateTime getEnd() {
    return end;
  }

  public void setEnd(DateTime end) {
    this.end = end;
  }

}
