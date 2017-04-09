package com.roundrobin.vault.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VendorMapTo {
  @JsonProperty("vendor_id")
  private String vendorId;
  private Double distance;
  private int minutes;

}
