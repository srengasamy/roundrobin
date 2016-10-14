package com.roundrobin.map.api;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.roundrobin.map.groups.MapValidator;

public class MapTo {
  @NotBlank(groups = MapValidator.class)
  private String skillId;

  @NotNull(groups = MapValidator.class)
  private GeoJsonPoint location;

  @NotNull(groups = MapValidator.class)
  private double radius;

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

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
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
