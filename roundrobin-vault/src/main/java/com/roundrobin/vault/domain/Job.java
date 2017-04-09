package com.roundrobin.vault.domain;

import com.roundrobin.vault.enums.JobStatus;
import com.roundrobin.vault.enums.VendorPreference;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Optional;

import lombok.Data;

@Data
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

}
