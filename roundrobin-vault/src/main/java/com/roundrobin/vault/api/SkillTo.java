package com.roundrobin.vault.api;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.roundrobin.vault.groups.CreateSkillValidator;
import com.roundrobin.vault.groups.SkillValidator;
import com.roundrobin.vault.groups.UpdateSkillValidator;
import com.roundrobin.vault.validator.CostRequired;

@CostRequired(groups = SkillValidator.class, message = "Invalid cost value")
@JsonInclude(Include.NON_ABSENT)
public class SkillTo {
  @NotBlank(groups = UpdateSkillValidator.class)
  private String id;

  @NotBlank(groups = CreateSkillValidator.class)
  private String skillDetailId;

  @NotBlank(groups = CreateSkillValidator.class)
  private Optional<String> userId = Optional.empty();

  @UnwrapValidatedValue
  @NotNull(groups = CreateSkillValidator.class)
  @Range(min = 10, max = 600, groups = SkillValidator.class)
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

  public Optional<String> getUserId() {
    return userId;
  }

  public void setUserId(Optional<String> userId) {
    this.userId = userId;
  }

}
