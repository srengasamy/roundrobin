package com.roundrobin.map.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.roundrobin.geo.GeoJsonDeserializer;
import com.roundrobin.map.groups.VendorValidator;

@JsonInclude(Include.NON_ABSENT)
public class VendorTo {
  @NotNull(groups = VendorValidator.class)
  @JsonProperty("vendor_id")
  private String vendorId;

  @NotNull(groups = VendorValidator.class)
  @JsonDeserialize(using = GeoJsonDeserializer.class)
  private GeoJsonPoint location;

  @NotNull(groups = VendorValidator.class)
  private List<String> skills;

  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public GeoJsonPoint getLocation() {
    return location;
  }

  public void setLocation(GeoJsonPoint location) {
    this.location = location;
  }

  public List<String> getSkills() {
    return skills;
  }

  public void setSkills(List<String> skills) {
    this.skills = skills;
  }


}
