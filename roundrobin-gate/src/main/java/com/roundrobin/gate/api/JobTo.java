package com.roundrobin.gate.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.roundrobin.gate.config.GeoJsonDeserializer;
import com.roundrobin.gate.enums.JobStatus;
import com.roundrobin.gate.enums.VendorPreference;
import com.roundrobin.gate.groups.CreateJobValidator;
import com.roundrobin.gate.groups.JobValidator;
import com.roundrobin.gate.groups.UpdateJobValidator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class JobTo {
  @NotBlank(groups = UpdateJobValidator.class)
  private String id;

  @JsonProperty("skill_detail_id")
  @NotNull(groups = CreateJobValidator.class)
  private String skillDetailId;

  @JsonProperty("location")
  @NotNull(groups = CreateJobValidator.class)
  @JsonDeserialize(using = GeoJsonDeserializer.class)
  private GeoJsonPoint location;

  @JsonProperty("vendor_pref")
  @NotNull(groups = CreateJobValidator.class)
  private VendorPreference vendorPref;

  @JsonProperty("preferred_start")
  private DateTime preferredStart;

  @JsonProperty("preferred_end")
  private DateTime preferredEnd;

  @Length(max = 1000, groups = JobValidator.class)
  private String desc;

  private JobStatus status;

  private DateTime created;
}
