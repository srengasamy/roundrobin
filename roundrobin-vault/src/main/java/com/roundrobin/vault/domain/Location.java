package com.roundrobin.vault.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

import lombok.Data;

@Data
@Document(collection = "location")
public class Location {
  @Id
  private String id;
  private GeoJsonPoint location;
  private UserProfile userProfile;
  private Date created;

}
