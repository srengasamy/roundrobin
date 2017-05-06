package com.roundrobin.gate.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.gate.enums.DeliveryType;
import com.roundrobin.gate.groups.CreateSkillDetailValidator;
import com.roundrobin.gate.groups.SkillDetailValidator;
import com.roundrobin.gate.groups.UpdateSkillDetailValidator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class SkillDetailTo {
  @NotBlank(groups = UpdateSkillDetailValidator.class)
  private String id;

  @UnwrapValidatedValue
  @NotBlank(groups = CreateSkillDetailValidator.class)
  @Length(max = 25, groups = SkillDetailValidator.class)
  private Optional<String> name = Optional.empty();

  @UnwrapValidatedValue
  @JsonProperty("delivery_type")
  @NotNull(groups = CreateSkillDetailValidator.class)
  private Optional<DeliveryType> deliveryType = Optional.empty();

  @JsonProperty("skill_group_id")
  @NotBlank(groups = CreateSkillDetailValidator.class)
  private String skillGroupId;

}
