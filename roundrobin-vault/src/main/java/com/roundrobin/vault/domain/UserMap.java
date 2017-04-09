package com.roundrobin.vault.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "user_map")
public class UserMap {
  @Id
  private String id;
  private UserProfile userProfile;
  private GeoJsonPoint location;
  
}
