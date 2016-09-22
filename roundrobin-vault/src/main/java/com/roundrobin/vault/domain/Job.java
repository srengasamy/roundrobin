package com.roundrobin.vault.domain;

import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job")
public class Job {
  @Id
  @Indexed
  private String id;
  @DBRef
  private UserProfile userProfile;
  @DBRef
  private SkillDetail skillDetail;
  private VendorPreference vendorPref;
  private GeoJsonPoint location;
  private JobStatus status;
  private Optional<DateTime> startWindow = Optional.empty();
  private Optional<DateTime> endWindow = Optional.empty();
  private JobTimeline timeline;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SkillDetail getSkillDetail() {
    return skillDetail;
  }

  public void setSkillDetail(SkillDetail skillDetail) {
    this.skillDetail = skillDetail;
  }

  public VendorPreference getVendorPref() {
    return vendorPref;
  }

  public void setVendorPref(VendorPreference vendorPref) {
    this.vendorPref = vendorPref;
  }

  public GeoJsonPoint getLocation() {
    return location;
  }

  public void setLocation(GeoJsonPoint location) {
    this.location = location;
  }

  public JobStatus getStatus() {
    return status;
  }

  public void setStatus(JobStatus status) {
    this.status = status;
  }

  public JobTimeline getTimeline() {
    return timeline;
  }

  public void setTimeline(JobTimeline timeline) {
    this.timeline = timeline;
  }

  public Optional<DateTime> getStartWindow() {
    return startWindow;
  }

  public void setStartWindow(Optional<DateTime> startWindow) {
    this.startWindow = startWindow;
  }

  public Optional<DateTime> getEndWindow() {
    return endWindow;
  }

  public void setEndWindow(Optional<DateTime> endWindow) {
    this.endWindow = endWindow;
  }

  public UserProfile getUserProfile() {
    return userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  public static enum JobStatus {
    REQUESTED, SCHEDULED, COMPLETED, CANCELLED;
  }
  
  public static enum VendorPreference {
    TOP_REVIEWED, CHEAP, NONE, URGENT
  }
}
