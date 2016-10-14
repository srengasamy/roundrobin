package com.roundrobin.map.api;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.roundrobin.geo.GeoJsonDeserializer;
import com.roundrobin.map.groups.TrailValidator;

@JsonInclude(Include.NON_ABSENT)
public class TrailTo {
  @JsonProperty("trail_id")
  private String trailId;
  
  private String vendorId;

  @NotNull(groups = TrailValidator.class)
  @JsonDeserialize(using = GeoJsonDeserializer.class)
  private GeoJsonPoint point;

  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public GeoJsonPoint getPoint() {
    return point;
  }

  public void setPoint(GeoJsonPoint point) {
    this.point = point;
  }

  public String getTrailId() {
    return trailId;
  }

  public void setTrailId(String trailId) {
    this.trailId = trailId;
  }


}
