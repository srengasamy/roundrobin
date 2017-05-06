package com.roundrobin.gate.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by rengasu on 5/1/17.
 */
@Data
@Document(collection = "user_profile_1")
public class UserProfile {
  @Id
  private String id;

  private Integer flags;
  private GeoJsonPoint location;
  private Short clockIn;
  private Short clockOut;
  private Short overtime;
  private Short radius;
  private DateTime created;

  @DBRef
  private List<Skill> skills = new ArrayList<>();
}
