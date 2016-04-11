package com.roundrobin.domain;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document(collection = "location")
public class Location {
  @Id
  private String id;
  private GeoJsonPoint location;
  private User user;
  @CreatedDate
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private Date created;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GeoJsonPoint getLocation() {
    return location;
  }

  public void setLocation(GeoJsonPoint location) {
    this.location = location;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

}
