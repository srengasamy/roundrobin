package com.roundrobin.api;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.groups.CreateSkillValidator;
import com.roundrobin.groups.SkillValidator;
import com.roundrobin.groups.UpdateSkillValidator;
import com.roundrobin.validator.CostRequired;

@CostRequired(groups = SkillValidator.class)
@JsonInclude(Include.NON_ABSENT)
public class SkillTo {
  @NotBlank(groups = UpdateSkillValidator.class)
  private String id;

  @NotBlank(groups = CreateSkillValidator.class)
  private String skillDetailId;

  @NotBlank(groups = SkillValidator.class)
  private String userProfileId;

  @UnwrapValidatedValue
  @NotNull(groups = CreateSkillValidator.class)
  private Optional<Integer> timeToComplete = Optional.empty();

  private Optional<Double> cost = Optional.empty();
  private Optional<Double> minCost = Optional.empty();
  private Optional<Double> maxCost = Optional.empty();
  private Double averageReview;
  private Integer numberOfReview;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSkillDetailId() {
    return skillDetailId;
  }

  public void setSkillDetailId(String skillDetailId) {
    this.skillDetailId = skillDetailId;
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

  public String getUserProfileId() {
    return userProfileId;
  }

  public void setUserProfileId(String userProfileId) {
    this.userProfileId = userProfileId;
  }

}
