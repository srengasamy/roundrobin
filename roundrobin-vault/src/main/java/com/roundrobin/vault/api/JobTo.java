package com.roundrobin.vault.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.core.api.User;
import com.roundrobin.vault.enums.JobStatus;
import com.roundrobin.vault.enums.VendorPreference;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Optional;

import lombok.Data;

@Data
public class JobTo {
  private String jobId;

  @JsonProperty("skill_detail_id")
  private String skillDetailId;
  private User user;
  private GeoJsonPoint location;

  @JsonProperty("vendor_pref")
  private VendorPreference vendorPref;

  @JsonProperty("start_date")
  private Optional<DateTime> startDate;

  @JsonProperty("emd_date")
  private Optional<DateTime> endDate;

  private JobStatus status;

}
