package com.roundrobin.vault.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "skill")
public class Skill {
  @Id
  private String id;

  @DBRef
  private SkillDetail skillDetails;
  private Integer timeToComplete;
  private Double cost;
  private Double minCost;
  private Double maxCost;
  private boolean active;
  private DateTime created;

  @DBRef
  private UserProfile profile;
}
