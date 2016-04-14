package com.roundrobin.domain;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roundrobin.groups.CreateSkillValidator;
import com.roundrobin.groups.SkillValidator;
import com.roundrobin.groups.UpdateSkillValidator;
import com.roundrobin.validator.CostRequired;

@Document(collection = "skill")
@CostRequired(groups = SkillValidator.class)
public class Skill {
  @Id
  @NotBlank(groups = UpdateSkillValidator.class)
  private String id;

  @Valid
  @DBRef
  @NotNull(groups = CreateSkillValidator.class)
  private SkillDetail skillDetails;

  @UnwrapValidatedValue
  @NotNull(groups = CreateSkillValidator.class)
  private Optional<Integer> timeToComplete = Optional.empty();

  @UnwrapValidatedValue
  private Optional<Double> cost = Optional.empty();

  @UnwrapValidatedValue
  private Optional<Double> minCost = Optional.empty();

  @UnwrapValidatedValue
  private Optional<Double> maxCost = Optional.empty();

  private Double averageReview;
  private Integer numberOfReview;

  @UnwrapValidatedValue
  @NotNull(groups = SkillValidator.class)
  private Optional<Boolean> active = Optional.empty();
  private DateTime created;

  @Version
  @JsonIgnore
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

  public Double getAverageReview() {
    return averageReview;
  }

  public void setAverageReview(Double averageReview) {
    this.averageReview = averageReview;
  }

  public Optional<Integer> getTimeToComplete() {
    return timeToComplete;
  }

  public void setTimeToComplete(Optional<Integer> timeToComplete) {
    this.timeToComplete = timeToComplete;
  }

  public Optional<Double> getCost() {
    return cost;
  }

  public void setCost(Optional<Double> cost) {
    this.cost = cost;
  }

  public Optional<Double> getMinCost() {
    return minCost;
  }

  public void setMinCost(Optional<Double> minCost) {
    this.minCost = minCost;
  }

  public Optional<Double> getMaxCost() {
    return maxCost;
  }

  public void setMaxCost(Optional<Double> maxCost) {
    this.maxCost = maxCost;
  }

  public Optional<Boolean> getActive() {
    return active;
  }

  public void setActive(Optional<Boolean> active) {
    this.active = active;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public Integer getNumberOfReview() {
    return numberOfReview;
  }

  public void setNumberOfReview(Integer numberOfReview) {
    this.numberOfReview = numberOfReview;
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
