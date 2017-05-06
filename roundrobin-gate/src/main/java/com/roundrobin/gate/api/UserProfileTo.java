package com.roundrobin.gate.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.roundrobin.gate.config.GeoJsonDeserializer;
import com.roundrobin.gate.groups.CreateProfileValidator;
import com.roundrobin.gate.groups.ProfileValidator;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by rengasu on 5/1/17.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class UserProfileTo {
  @NotNull(groups = CreateProfileValidator.class)
  @JsonDeserialize(using = GeoJsonDeserializer.class)
  private GeoJsonPoint location;

  @JsonProperty("clock_in")
  @NotNull(groups = CreateProfileValidator.class)
  @Range(min = 0, max = 1440, groups = ProfileValidator.class)
  private Short clockIn;

  @JsonProperty("clock_out")
  @NotNull(groups = CreateProfileValidator.class)
  @Range(min = 0, max = 1440, groups = ProfileValidator.class)
  private Short clockOut;

  @JsonProperty("overtime")
  @NotNull(groups = CreateProfileValidator.class)
  @Range(min = 15, max = 1440, groups = ProfileValidator.class)
  private Short overtime;

  @JsonProperty("radius")
  @NotNull(groups = CreateProfileValidator.class)
  @Range(min = 5, max = 100, groups = ProfileValidator.class)
  private Short radius;
}
