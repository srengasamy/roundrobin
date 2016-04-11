package com.roundrobin.domain;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.roundrobin.validator.CostRequired;

@Document(collection = "skill")
@CostRequired
public class Skill {
  @Id
  private String id;
  private SkillDetail skillDetails;
  private int timeToComplete;
  private float cost;
  private float minCost;
  private float maxCost;
  private GeoJsonPoint location;
  private float averageReview;
  private int numberOfReview;
  private boolean active;
  @CreatedDate
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private Date created;

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

  public float getAverageReview() {
    return averageReview;
  }

  public void setAverageReview(float averageReview) {
    this.averageReview = averageReview;
  }

  public int getNumberOfReview() {
    return numberOfReview;
  }

  public void setNumberOfReview(int numberOfReview) {
    this.numberOfReview = numberOfReview;
  }

  public float getCost() {
    return cost;
  }

  public void setCost(float cost) {
    this.cost = cost;
  }

  public float getMinCost() {
    return minCost;
  }

  public void setMinCost(float minCost) {
    this.minCost = minCost;
  }

  public float getMaxCost() {
    return maxCost;
  }

  public void setMaxCost(float maxCost) {
    this.maxCost = maxCost;
  }

  public GeoJsonPoint getLocation() {
    return location;
  }

  public void setLocation(GeoJsonPoint location) {
    this.location = location;
  }

  public int getTimeToComplete() {
    return timeToComplete;
  }

  public void setTimeToComplete(int timeToComplete) {
    this.timeToComplete = timeToComplete;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public static enum DeliveryType {
    HOME, STORE, ONLINE;
  }
  
  public static enum PricingType {
    CHEAP, QUALITY, BEST;
  }
}
