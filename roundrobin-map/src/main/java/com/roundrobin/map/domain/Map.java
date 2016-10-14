package com.roundrobin.map.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "map")
public class Map {
  @Id
  private String id;

  @DBRef
  private Vendor vendor;

  @DBRef
  @Indexed
  private GeoJsonPoint point;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Vendor getVendor() {
    return vendor;
  }

  public void setVendor(Vendor vendor) {
    this.vendor = vendor;
  }

  public GeoJsonPoint getPoint() {
    return point;
  }

  public void setPoint(GeoJsonPoint point) {
    this.point = point;
  }


}
