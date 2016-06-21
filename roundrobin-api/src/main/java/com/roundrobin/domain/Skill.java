package com.roundrobin.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
  private Double averageReview;
  private Integer numberOfReview;
  private Boolean active;
  private DateTime created;
  @Version
  private Long version;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SkillDetail getSkillDetails() {
    return skillDetails;
  }

  public void setSkillDetails(SkillDetail skillDetails) {
    this.skillDetails = skillDetails;
  }

  public Integer getTimeToComplete() {
    return timeToComplete;
  }

  public void setTimeToComplete(Integer timeToComplete) {
    this.timeToComplete = timeToComplete;
  }

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public Double getMinCost() {
    return minCost;
  }

  public void setMinCost(Double minCost) {
    this.minCost = minCost;
  }

  public Double getMaxCost() {
    return maxCost;
  }

  public void setMaxCost(Double maxCost) {
    this.maxCost = maxCost;
  }

  public Double getAverageReview() {
    return averageReview;
  }

  public void setAverageReview(Double averageReview) {
    this.averageReview = averageReview;
  }

  public Integer getNumberOfReview() {
    return numberOfReview;
  }

  public void setNumberOfReview(Integer numberOfReview) {
    this.numberOfReview = numberOfReview;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public static enum DeliveryType {
    HOME, STORE, ONLINE;
  }

  public static enum PricingType {
    CHEAP, QUALITY, BEST;
  }
}
