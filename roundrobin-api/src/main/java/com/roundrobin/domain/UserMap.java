package com.roundrobin.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_map")
public class UserMap {
  @Id
  private String id;
  private UserProfile userProfile;
  private GeoJsonPoint location;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UserProfile getUserProfile() {
    return userProfile;
  }

  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  public GeoJsonPoint getLocation() {
    return location;
  }

  public void setLocation(GeoJsonPoint location) {
    this.location = location;
  }


}
