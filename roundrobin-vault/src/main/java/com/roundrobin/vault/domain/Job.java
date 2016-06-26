package com.roundrobin.vault.domain;

public class Job {
  public static enum JobStatus {
    REQUESTED, SCHEDULED, COMPLETED, CANCELLED;
  }
  public static enum UrgencyType {
    URGENT, BEST, RELAXED;
  }
  public static enum PaymentStatus {
    PENDING, COMPLETED;
  }
}
