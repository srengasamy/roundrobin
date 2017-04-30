package com.roundrobin.vault.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.roundrobin.vault.enums.DeliveryType;
import com.roundrobin.vault.groups.CreateSkillDetailValidator;
import com.roundrobin.vault.groups.SkillDetailValidator;
import com.roundrobin.vault.groups.UpdateSkillDetailValidator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
