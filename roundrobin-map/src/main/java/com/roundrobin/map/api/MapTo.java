package com.roundrobin.map.api;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.roundrobin.geo.GeoJsonDeserializer;
import com.roundrobin.map.groups.MapValidator;
@JsonInclude(Include.NON_ABSENT)
public class MapTo {
  @NotBlank(groups = MapValidator.class)
  @JsonProperty("skill_id")
  private String skillId;

  @NotNull(groups = MapValidator.class)
  @JsonDeserialize(using = GeoJsonDeserializer.class)
  private GeoJsonPoint location;

  @NotNull(groups = MapValidator.class)
  private Double radius;

  private String vendorId;
  private int minutes;

  public MapTo() {

  }

  public MapTo(String vendorId, int minutes) {
    this.vendorId = vendorId;
    this.minutes = minutes;
  }

  public String getSkillId() {
    return skillId;
  }

  public void setSkillId(String skillId) {
    this.skillId = skillId;
  }

  public GeoJsonPoint getLocation() {
    return location;
  }

  public void setLocation(GeoJsonPoint location) {
    this.location = location;
  }

  public Double getRadius() {
    return radius;
  }

  public void setRadius(Double radius) {
    this.radius = radius;
  }

  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public int getMinutes() {
    return minutes;
  }

  public void setMinutes(int minutes) {
    this.minutes = minutes;
  }

}
