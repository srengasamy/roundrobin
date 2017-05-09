package com.roundrobin.gate.domain;

import com.roundrobin.gate.enums.JobStatus;
import com.roundrobin.gate.enums.VendorPreference;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "job")
public class Job {
  @Id
  private String id;

  @DBRef
  private UserProfile profile;

  @DBRef
  private SkillDetail skillDetail;

  private VendorPreference vendorPref;
  private GeoJsonPoint location;
  private String desc;
  private JobStatus status;

  private DateTime preferredStart;

  private DateTime preferredEnd;

  private DateTime created;

  private boolean active;
}
