package com.roundrobin.vault.api;

import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.roundrobin.api.User;
import com.roundrobin.vault.domain.Job.JobStatus;
import com.roundrobin.vault.domain.Job.VendorPreference;

public class JobTo {
  private String jobId;
  private String skillDetailId;
  private User user;
  private GeoJsonPoint location;
  private VendorPreference vendorPref;
  private Optional<DateTime> startDate;
  private Optional<DateTime> endDate;
  private JobStatus status;

  public String getSkillDetailId() {
    return skillDetailId;
  }

  public void setSkillDetailId(String skillDetailId) {
    this.skillDetailId = skillDetailId;
  }

  public GeoJsonPoint getLocation() {
    return location;
  }

  public void setLocation(GeoJsonPoint location) {
    this.location = location;
  }

  public VendorPreference getVendorPref() {
    return vendorPref;
  }

  public void setVendorPref(VendorPreference vendorPref) {
    this.vendorPref = vendorPref;
  }

  public Optional<DateTime> getStartDate() {
    return startDate;
  }

  public void setStartDate(Optional<DateTime> startDate) {
    this.startDate = startDate;
  }

  public Optional<DateTime> getEndDate() {
    return endDate;
  }

  public void setEndDate(Optional<DateTime> endDate) {
    this.endDate = endDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public JobStatus getStatus() {
    return status;
  }

  public void setStatus(JobStatus status) {
    this.status = status;
  }

}
