package com.roundrobin.gate.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.gate.groups.CreateSkillValidator;
import com.roundrobin.gate.groups.SkillValidator;
import com.roundrobin.gate.groups.UpdateSkillValidator;
import com.roundrobin.gate.validator.CostRequired;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@CostRequired(groups = SkillValidator.class, message = "Invalid cost value")
@JsonInclude(Include.NON_ABSENT)
public class SkillTo {
  @NotBlank(groups = UpdateSkillValidator.class)
  private String id;

  @JsonProperty("skill_detail_id")
  @NotBlank(groups = CreateSkillValidator.class)
  private String skillDetailId;

  @UnwrapValidatedValue
  @JsonProperty("time_to_complete")
  @NotNull(groups = CreateSkillValidator.class)
  @Range(min = 10, max = 600, groups = SkillValidator.class)
  private Optional<Integer> timeToComplete = Optional.empty();

  private Optional<Double> cost = Optional.empty();

  @JsonProperty("min_cost")
  private Optional<Double> minCost = Optional.empty();

  @JsonProperty("max_cost")
  private Optional<Double> maxCost = Optional.empty();

}
